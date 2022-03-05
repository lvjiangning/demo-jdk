## ThreadPoolExecutor



### 成员变量说明

```java
// ctl存储线程池状态和线程数，integer共32位，那么用前三位表示线程池状态，后29位表示线程池数量。
// 初始化，状态为RUNNING，起始线程数是0。
private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
// 这个就代表初始值 29位 (Integer.SIZE=32)
private static final int COUNT_BITS = Integer.SIZE - 3;
// 最大支持线程数 2^29-1
private static final int CAPACITY = (1 << COUNT_BITS) - 1;
// 以下为线程池的四个状态，用32位中的前三位表示
// 111 00000000000000000000000000000
private static final int RUNNING= -1 << COUNT_BITS; 
// 000 00000000000000000000000000000 拒绝新的任务提交,会将队列中的任务执行完事,正在执行的任务继续执行.
private static final int SHUTDOWN =0 << COUNT_BITS;
// 001 00000000000000000000000000000 拒绝新的任务提交,清空在队列中的任务
private static final int STOP =1 << COUNT_BITS;
// 010 00000000000000000000000000000 所有任务都销毁了,workCount=0的时候,线程池的装填在转换为TIDYING时,会执行钩子方法terminated()
private static final int TIDYING=2 << COUNT_BITS;
// 011 00000000000000000000000000000 terminated() 方法执行完成后,线程池的状态会转为TERMINATED.
private static final int TERMINATED =3 << COUNT_BITS;
// 获取当前线程池的状态(前3位)
private static int runStateOf(int c) { return c & ~CAPACITY; }
// 获取当前线程池中线程数(后29位)
private static int workerCountOf(int c){ return c & CAPACITY; }
// 更新状态和数量
private static int ctlOf(int rs, int wc) { return rs | wc; }
// 小于判断C是不是小于S,比如runStateLessThan(var,STOP),那var就只有可能是(RUNNING,SHUTDOWN)
private static boolean runStateLessThan(int c, int s) {
    return c < s;
}
// 是不是C >= S
private static boolean runStateAtLeast(int c, int s) {
    return c >= s;
}
// 判断状态是不是RUNNING
private static boolean isRunning(int c) {
    return c < SHUTDOWN;
}
// execute()方法提交的Runnable任务,如果当前没有获取到线程去执行任务,那么任务将放到这个阻塞队列中.
private final BlockingQueue<Runnable> workQueue;
// 这个锁用来保护下面的workers,访问workers必须获取这个锁.
private final ReentrantLock mainLock = new ReentrantLock();

// 设置包含池中的所有工作线程, 只有在持有主锁时才能访问.
- 这里为什么不使用线程安全的数据结构的原因主要两个:
1. 有复合操作, 增加 worker 的同时还要更新 largestPoolSize.
2. 中断线程时,如果不加锁,就可能出现并发的中断线程,引起中断风暴.
private final HashSet<Worker> workers = new HashSet<Worker>();
// 线程通信手段, 用于支持awaitTermination方法, awaitTermination的作用等待所有任务完成,并支持设置超时时间,返回值代表是不是超时.
private final Condition termination = mainLock.newCondition();
// 记录workers历史以来的最大值,每次获取之前都要获取主锁mainlock
// 每次增加worker的时候,都会判断当前workers.size()是否大于largestPoolSize,如果大于,则将当前最大值赋予largestPoolSize.
private int largestPoolSize;
// 计数所有已完成任务,每次获取之前都要获取主锁mainlock
// 每个worker都有一个自己的成员变量 completedTasks 来记录当前 worker 执行的任务次数, 当前线worker工作线程终止的时候, 才会将worker中的completedTasks的数量加入到 completedTaskCount 指标中.
private long completedTaskCount;
// 线程工厂,用于构造线程的时候加一些业务标识什么的.
private volatile ThreadFactory threadFactory;
// 拒绝策略,默认四种AbortPolicy、CallerRunsPolicy、DiscardPolicy、DiscardOldestPolicy,建议自己实现,增加监控指标.
private volatile RejectedExecutionHandler handler;
// 当线程池内线程数超过corePoolSize之后,空闲的线程多久之后进行销毁.
private volatile long keepAliveTime;
// 核心线程池空闲允许销毁的时间.
private volatile boolean allowCoreThreadTimeOut;
// 线程池核心线程池大小
private volatile int corePoolSize;
// 线程池可建立的最大线程数
private volatile int maximumPoolSize;
// 默认拒绝策略 AbortPolicy
private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();
//  安全控制访问（主要用于shutdown和 shutdownNow方法）
private static final RuntimePermission shutdownPerm = new RuntimePermission("modifyThread");
// threadPoolExecutor初始化的时候,还会初始化AccessControlContext对象的acc的值。
// 在threadPoolExecutor初始化的时候赋值,acc对象是指当前调用上下文的快照，其中包括当前线程继承的AccessControlContext和任何有限的特权范围，使得可以在稍后的某个时间点(可能在另一个线程中)检查此上下文。
private final AccessControlContext acc;
```

我们要清楚任务是Runnable(或者叫task或者command) , 线程是worker .

### 提交任务主流程分析

#### AbstractExecutorService.submit()

```java
public <T> Future<T> submit(Callable<T> task) {
        if (task == null) throw new NullPointerException();
        // 构造一个FutureTask实例,只要作用是获得返回值.这部分先不管,先看提交过程
        RunnableFuture<T> ftask = newTaskFor(task);
        //执行任务
        execute(ftask);
        return ftask;
    }
```

### ThreadPoolExecutor.execute()

```java
public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
        int c = ctl.get(); // 获取当前线程池状态及线程数容器
        if (workerCountOf(c) < corePoolSize) {// 判断当前线工作的线程数是否小于配置的核心线程数
            if (addWorker(command, true))// 如果小于核心线程数就增加worker，并且把command作为当前线程的第一个任务
                return; // 增加worker成功且worker跑起来了就返回
            c = ctl.get();// 线程池拒绝了增加worker, 重新获取线程池状态
        }
       // worker增加失败,或者当前线程池中线程数超过核心线程数.
        if (isRunning(c) && workQueue.offer(command)) {// 判断当前线程池的状态是不是RUNNING,如果是则将任务加入到阻塞队列, offer是不阻塞的.
            int recheck = ctl.get();// 获取当前线程池状态
            if (! isRunning(recheck) && remove(command))// 如果线程池已不处于 RUNNING 状态，那么移除已入队的任务，并且执行拒绝策略
                reject(command);//执行拒绝策略
            else if (workerCountOf(recheck) == 0)//查看当前工作线程的数量
                addWorker(null, false);//如果当前线程数是0,那么刚刚的任务肯定在阻塞队列里面了,这个时候开启一个没有任务的线程去跑.
        }
       // 当线程池当前的状态不是RUNNING,或者往workQueue添加worker失败
        else if (!addWorker(command, false))//这个时候,队列满了,或者状态不是RUNNING,再次尝试开启一个worker
            reject(command); //增加worker失败,执行拒绝策略.
    }
```

### ThreadPoolExecutor.addWorker(Runnable firstTask, boolean core)

```java
//创建新的线程执行当前任务 
//firstTask: 指定新增线程执行的第一个任务或者不执行任务
private boolean addWorker(Runnable firstTask, boolean core) {
        retry:
        for (;;) {
            int c = ctl.get();// 获取当前线程池状态及线程数的容器
            int rs = runStateOf(c);// 获取当前运行状态
            
            // Check if queue empty only if necessary.
            // 如果线程池状态是SHUTDOWN、STOP、TIDYING、TERMINATED就不允许提交。
            // && 后面的特殊情况，线程池的状态是SHUTDOWN并且要执行的任务为Null并且队列不是空，这种情况下是允许增加一个线程来帮助队列中的任务跑完的，因为shutdown状态下，允许执行完成阻塞队里中的任务
            if (rs >= SHUTDOWN &&! (rs == SHUTDOWN && firstTask == null &&! workQueue.isEmpty())) 
                return false;

            for (;;) {
                int wc = workerCountOf(c);// 当前线程数
                if (wc >= CAPACITY ||
                    wc >= (core ? corePoolSize : maximumPoolSize))// 是否超过当前约定的最大值，超过就拒绝加入，直接返回，这里你要看core传的是什么，也就是当前处于哪一种情形，是核心线程池没用完还是咋的。
                    return false;
                if (compareAndIncrementWorkerCount(c))//没超过约定值，那么通过CAS的方式增加worker的数量，增加成功就跳出外层循环。
                    break retry;
                c = ctl.get();  // 再次获取当前线程池状态+线程数容器
                if (runStateOf(c) != rs)// 判断当前运行状态是不是改变了
                    continue retry; //外层循环重新执行
                // runStateOf(c) != rs 这个判断操作主要是看当前线程池的状态变没变，
                // - 变了，就从外层循环重新执行，重新进行状态的检查。
                // - 没变，从当前循环重新执行，重新执行CAS操作。
                // else CAS failed due to workerCount change; retry inner loop
            }
        }

        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker w = null;
        try {
            w = new Worker(firstTask);//构建worker，并将当前任务赋值给当前worker,这个地方要看一下new Worker的源码，如下，你会发现会直接new一个线程给当前worker对象
       //Worker(Runnable firstTask) {
       //     setState(-1); // runWorker运行之前不允许中断
       //     this.firstTask = firstTask;
       //     this.thread = getThreadFactory().newThread(this); 一定要关注这个this
       // }
            final Thread t = w.thread; //获取当前worker的线程
            if (t != null) {
                final ReentrantLock mainLock = this.mainLock;//获取主锁
                mainLock.lock();//加锁
                try {
                    int rs = runStateOf(ctl.get());//获取当前运行状态

                    if (rs < SHUTDOWN ||//如果当前状态是<SHUTDOWN也就是RUNNABLE状态
                        (rs == SHUTDOWN && firstTask == null)) {//或者状态是SHUTDOWN并且当前任务是空的，这种就是单纯想新搞一个worker（thread），比如前面说的场景，阻塞队里里面还有，但当前已经是不允许提交的状态了。
                        if (t.isAlive()) // 检查当前线程是不是已经开始跑了
                            throw new IllegalThreadStateException();//刚刚new的线程怎么就抛弃了了，这种就抛异常，错误的线程状态异常。
                        workers.add(w);// 增加wokrer，这也就是为啥上面加锁的缘故，至于为啥要用hashSet，可以看上文成员变量wokers的说明。
                        int s = workers.size();//得到当前worker的总数
                        if (s > largestPoolSize)//当前worker的总数如果大于之前记录的线程池大小
                            largestPoolSize = s;//线程池大小重新赋值
                        workerAdded = true;//表示这次worker增加成功了
                    }
                } finally {
                    mainLock.unlock();// 释放主锁
                }
                if (workerAdded) {// 如果worker增加成功了
                    t.start();//线程就跑起来
                    workerStarted = true;//表示线程已经跑起来了
                }
            }
        } finally {
            if (! workerStarted)//判断worker线程是否正常跑起来了
                addWorkerFailed(w);//没成功跑起来，说明增加worker失败了
        }
        return workerStarted;//返回的是当前worker是否跑起来。
    }
```

调用 `t.start()` 后，将执行 Worker 中的 run 方法，为啥呢，看Worker对象构造这块, `3）` 这一步将 `Worker` 直接塞了进去，这么看就明白了吧。

```java
Worker(Runnable firstTask) {
     setState(-1);  // runWorker运行之前不允许中断
     this.firstTask = firstTask;
===> 3) this.thread = getThreadFactory().newThread(this);// 一定要关注这个this
 }
```

### Worker

好好看看Worker类，继承了AQS，实现了 Runnable。

**继承AQS的原因：** 线程只有两个状态，一个是独占表示线程正在运行，一个是不加锁空闲状态，用AQS的state区分，这里为啥不用ReentrantLock，主要原因是ReentrantLock是允许重入的。

```java
private final class Worker extends AbstractQueuedSynchronizer implements Runnable
```

### Woker.run()

```java
/** Delegates main run loop to outer runWorker  */
public void run() {
    runWorker(this);
}
```

### runWorker(this)

这里要说明一下, runWorker方法属于只要开启了一个线程,就一直循环执行getTask, getTask方法是可能阻塞的,当task==null&&getTask==null 这种就才会关闭当前任务.

```java
final void runWorker(Worker w) {
        Thread wt = Thread.currentThread(); //获取当前线程
        Runnable task = w.firstTask; //获取当前线程任务
        w.firstTask = null; //拿到任务后赋值给task，然后firstTask置为null。
        w.unlock();  //设置允许中断，与Worker构造那里的setState（-1）遥相呼应
        boolean completedAbruptly = true;//标识任务是不是立刻就完成了。
        try {
            while (task != null || (task = getTask()) != null) {//获取任务这个getTask单独讲
                w.lock();//尝试加锁
               
                //如果(线程池的状态>=STOP或者（线程已中断并且线程状态>=STOP）)并且当前线程没有被中断。
                // 两种情况:
                //1)如果当前线程池的状态是>=Stop的，并且当前线程没有被中断，那么就要执行中断。
                //2)或者当前线程目前是已中断的状态并且线程池的状态也是>=Stop的（注意Thread.interrupted是会擦除中断标识符的），那么因为中断标识符已经被擦除了，那么!wt.isInterrupted()一定返回true，这个时候还是要将当前线程中断。第二次执行runStateAtLeast(ctl.get(), STOP)相当于一个二次检查。
                if ((runStateAtLeast(ctl.get(), STOP) ||(Thread.interrupted() && runStateAtLeast(ctl.get(), STOP))) 
                    && !wt.isInterrupted())
                    wt.interrupt();//中断当前线程
                try {
                    beforeExecute(wt, task);//前置操作，空方法，可以业务自己实现
                    Throwable thrown = null;
                    try {
                        task.run();//执行任务，这个实际上调用的是传入的ftask的run方法
                    } catch (RuntimeException x) {
                        thrown = x; throw x;
                    } catch (Error x) {
                        thrown = x; throw x;
                    } catch (Throwable x) {
                        thrown = x; throw new Error(x);
                    } finally {
                        afterExecute(task, thrown);//后置操作,空方法,可以业务自己实现
                    }
                } finally {
                    task = null;//最后将task置为null
                    w.completedTasks++;//已完成的任务计数器+1
                    w.unlock();//释放当前线程的独占锁
                }
            }
            completedAbruptly = false;//任务执行完事,经过上面一堆过程,标识着任务不是立刻完成.
        } finally {
         // 一定要注意。执行到这里说明task == null并且getTask()返回null。说明当前线程池中不需要那么多线程来执行任务了，可以把多于corePoolSize数量的工作线程干掉,也可能被中断被SHUTDOWN等,也会导致getTask返回Null
            processWorkerExit(w, completedAbruptly);//任务退出过程,单独分析
        }
    }
```

### getTask

获取任务的方法

要注意的是调用getTask方法的地方是一个while死循环, 只要getTask有返回值,那么就不会退出循环.

退出循环就说明该销毁超过核心线程数的那部分线程了.

```java
private Runnable getTask() {
        boolean timedOut = false; //最后获取任务是不是超时了

        for (;;) {// 死循环
            int c = ctl.get();// 获取容器
            int rs = runStateOf(c);//获取当前的运行状态

            // 如果当前状态是>=SHOTDOWN状态&&(运行状态是STOP或者队列是空的).
            // 1)如果线程池的状态是>=STOP状态,这个时候不再处理队列中的任务,并且减少worker记录数量,返回的任务为null,这个时候在runRWorker方法中会执行processWorkerExit进行worker的退出操作.
            // 2)如果线程池的状态是>=SHUTDOWN并且workQueue为空,就说明处于SHOTdown以上的状态下,且没有任务在等待,那么也属于获取不到任务,getTask返回null.
            if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
                decrementWorkerCount();//减少线程池中线程的数量计数,在runWorker中finally中进行线程退出
                return null;//返回null
            }

            int wc = workerCountOf(c);//获取当前wokrer的数量

            // 是否开启超时机制.
            // 如果核心线程数允许超时,则timed为true,开启超时机制.
            // 如果核心线程数不允许超时,那么就看当前总线程数是不是>核心线程数,如果大于,则timed为true,当前woker开启超时机制.
            boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;

            // (如果当前线程池大小超过最大的设定值 或者 线程设置允许超时且当前woker获取任务超时) 并且当前线程池大小不是零或阻塞队列是空的,这种就返回null,并减少线程池线程计数.
            if ((wc > maximumPoolSize || (timed && timedOut))
                && (wc > 1 || workQueue.isEmpty())) {
                
                // 满足上述条件就减少worker计数.这里为啥不用decrementWorkerCount()呢,
                // 上面使用decrementWorkerCount()是因为确定不管是什么情况下,数量都要减,多减一次也没事,因为这个时候就是要关闭线程池释放资源.
                if (compareAndDecrementWorkerCount(c))//这里不一样,线程池的状态可能是RUNNING状态,多减一次,可能导致任务获取不到worker去跑.
                    return null;
                continue;//false 跳出本次循环重新检查.
            }

            try {
                //判断是否允许超时,允许超时用poll设置超时时间,不允许就使用take依赖超时机制
                Runnable r = timed ?
                    workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                    workQueue.take();
                if (r != null)
                    return r;//任务不等于null 就返回
                timedOut = true;//这种一定是超时导致的,所以timedOut设置为true
            } catch (InterruptedException retry) {//因为阻塞队列poll take都是使用快速响应中断的加锁方式(lockInterruptibly()),因此需要捕获中断异常.并且这方法是用的Thread.interrupted()判断的,有个特点会擦除中断状态,这就说明getTask方法是不响应中断的.
                timedOut = false;
            }
        }
    }
```

### processWorkerExit

任务异常退出, 则再加个worker回来, 当前任务是丢了的.

任务不是异常退出:

\1) 如何核心线程允许超时,当任务队列中还有任务,那么就必须保证线程池中有一个worker,没有就在这个方法里面执行addWorker.

\2) 如果核心线程不允许超时,就得保证当前线程池中线程数量>=核心线程数,如果当前线程池中线程数量<核心线程数,依然要增加一个worker,执行addWorker.

```java
private void processWorkerExit(Worker w, boolean completedAbruptly) {
    //任务是不是突然完成啦,完成就将工作线程数量-1
    //如果completedAbruptly为true，则说明线程执行时出现异常，需要将workerCount数量减一
    //如果completedAbruptly为false，说明在getTask方法中已经对workerCount进行减一，这里不用再减
    if (completedAbruptly)
        decrementWorkerCount();

    final ReentrantLock mainLock = this.mainLock;
    mainLock.lock();
    try {
        //更新已完成任务的数量的统计项
        completedTaskCount += w.completedTasks;
        //从worker中移除work
        workers.remove(w);
    } finally {
        mainLock.unlock();
    }
    //尝试关闭线程池,但如果是正常运行状态,就不会关闭,这个是否关闭的临界条件在分析tryTerminate单独会说.
    tryTerminate();

    int c = ctl.get();
  
    //这个地方比较绕,要好好看哈.
    // completedAbruptly=true代表异常结束的,具体为啥可以看runWorker中的代码,没有异常的话会走completedAbruptly=false的.
    //前提当前线程池的状态是SHUGTDOWN或者RUNNING,如果不是这两个状态,说明线程已经停止了,啥都不会要干了.
    //如果任务是异常结束的,就增加worker
    //注: 别问我为啥上面要删除worker,还要再加,不删是不是不用加了. 明确下那个任务已经退出getTask那块的死循环了,永远回不去了,只能新增worker.
    if (runStateLessThan(c, STOP)) {
        if (!completedAbruptly) {
            //走到这说明不是异常退出的
            int min = allowCoreThreadTimeOut ? 0 : corePoolSize;
            if (min == 0 && !workQueue.isEmpty())//如果允许核心线程超时并且当前队列里面还有任务没跑呢,那就必须留一个线程,不能全死掉.
                min = 1;
            if (workerCountOf(c) >= min)
                return; // replacement not needed
        }
        addWorker(null, false);
    }
}
```

### tryTerminate

interruptIdleWorkers(ONLY_ONE); 是否好奇为啥这里只中断一个worker呢, 这里就涉及到了线程池的优雅退出了.

当执行到 `===>(1` 的时候, 线程池只能处于两种状态:

\1) `STOP` 状态 , 这个时候 workQueue 可能是有值的 , workQueue 在清空的过程中了.

\2) `SHUTDOWN` 状态并且 workQueue 是空的 .

这两种状态都是说明, 线程池即将关闭, 或者说空闲的线程此时已经没用了,这个时候随手关一个, 反正要关,早关晚关而已.

```java
//根据线程池状态判断是否结束线程池
final void tryTerminate() {
    for (; ; ) {
        int c = ctl.get();
        //RUNNING状态,不能终止线程池
        //线程池状态是TIDYING或TERMINATED说明线程池已经处于正在终止的路上,不用再终止了.
        //状态为SHUTDOWN，但是任务队列不为空,也不能终止线程池
        if (isRunning(c) ||
                runStateAtLeast(c, TIDYING) ||
                (runStateOf(c) == SHUTDOWN && !workQueue.isEmpty()))
            return;
        //工作线程数量不等于0，中断一个空闲的工作线程并返回
===>(1   //这个时候线程池一定是STOP的状态或者SHUTDOW并队列不为空,这两种情况要么就是尝试终止因此这个时候尝试中断一个空闲worker
        if (workerCountOf(c) != 0) {
            interruptIdleWorkers(ONLY_ONE);
            return;
        }

        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            // 设置线程池状态为TIDYING，如果设置成功，则调用terminated方法
            if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
                try {
                    //子类实现
                    terminated();
                } finally {
                    // 设置状态为TERMINATED
                    ctl.set(ctlOf(TERMINATED, 0));
                    termination.signalAll();
                }
                return;
            }
        } finally {
            mainLock.unlock();
        }
    }
}
```

### interruptIdleWorker

```java
//中断空闲线程,因为空闲线程一定都是通过LockSupport.park使其处于等待状态,此时如果执行中断interrupt命令,当前空闲线程会即刻抛出中断异常,并被唤醒.
private void interruptIdleWorkers(boolean onlyOne) {
    final ReentrantLock mainLock = this.mainLock;
    mainLock.lock();
    try {
        //遍历worker，根据onlyOne判断，如果为ture只中断一个空闲线程
        for (Worker w : workers) {
            Thread t = w.thread;
            //线程没有被中断并且线程是空闲状态tryLock()判断是否空闲
            if (!t.isInterrupted() && w.tryLock()) {
                try {
                    t.interrupt();
                } catch (SecurityException ignore) {
                } finally {
                    w.unlock();
                }
            }
            if (onlyOne)
                break;
        }
    } finally {
        mainLock.unlock();
    }
}
```

### 指标获取

### 实时指标获取

threadPoolExecutor.getActiveCount(): 返回正在执行任务的`大致`线程数 , 注意: 不是精确值, 所以目前看作用就只有展示用, 不能作为临界条件使用.

```java
public int getActiveCount() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();//阻止新的worker加入并执行
        try {
            int n = 0;
            for (Worker w : workers)//当前循环的过程,worker的状态也是不断状态的比如会出现刚刚统计完,但没统计结束,他就跑完了,这种就统计错了.
                if (w.isLocked())
                    ++n;
            return n;
        } finally {
            mainLock.unlock();
        }
    }
```

threadPoolExecutor.getCompletedTaskCount() : 返回已执行完成的任务的`大致`总数,为啥不精确,原因与`getActiveCount`相似, 就不分析了.

threadPoolExecutor.getTaskCount(): 返回计划执行的任务的`大致`总数, 为啥不精确, 原因与`getActiveCount`相似, 就不分析了.

threadPoolExecutor.getLargestPoolSize(): 返回在池中历史创建的最大线程数.

threadPoolExecutor.getPoolSize(): 返回池中当前的线程数.

### 固定指标获取

> 线程池构造的时候指定的

threadPoolExecutor.getRejectedExecutionHandler(): 返回不可执行任务的当前处理程序

threadPoolExecutor.getThreadFactory(): 返回用于创建新线程的线程工厂

threadPoolExecutor.getCorePoolSize(): 返回核心线程数

threadPoolExecutor.getKeepAliveTime(): 返回线程保持活动时间

threadPoolExecutor.getMaximumPoolSize(): 返回允许的最大线程数

threadPoolExecutor.getQueue(): 返回此执行程序使用的任务队列

### 拒绝策略



AbortPolicy、抛RejectedExecutionException异常（默认的处理方式）

CallerRunsPolicy、 调用execute()方法拒绝任务

DiscardPolicy、悄悄地丢弃拒绝的任务

DiscardOldestPolicy 丢弃最老的未处理的请求，然后重新执行execute()方法

### 拒绝策略建议自己实现

拒绝策略建议自己实现,如下, 加上实时统计指标, 方便对线程池容量, 以及使用是否合理进行分析, 这一点真的在实战中特别重要.

```java
public class MonitoredCallerRunsPolicy implements RejectedExecutionHandler {

    private final static Logger LOG = LoggerFactory.getLogger(MonitoredCallerRunsPolicy.class);

    private final String threadPoolName;

    public final static String THREAD_CALLER_RUN = "ThreadPoolCallerRun";

    public MonitoredCallerRunsPolicy(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor threadPoolExecutor) {
        if (!threadPoolExecutor.isShutdown()) {
            r.run();
            // todo 增加必要实时监控指标
            LOG.info("thread pool:{} trigger caller Run", threadPoolName);
            Cat.logEvent(THREAD_CALLER_RUN, threadPoolName);
        }
    }
}
```

### 其他方法说明

### shutdown

关闭线程池，不再接受新的任务，已提交执行的任务继续执行。

注意: 正在运行的任务, 运行完事并且当前workQueue已经是空的了, 就直接退出了.线程走向终结.具体源码为getTask的

```java
if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
                decrementWorkerCount();
                return null;//返回null就要执行processWorkerExit退出的逻辑了.
            }
public void shutdown() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            checkShutdownAccess();//安全策略判断
            advanceRunState(SHUTDOWN);//RUNNING->SHUTDOWN状态转换
            interruptIdleWorkers();//中断所有空闲线程,这个方法上面分析了
            onShutdown(); // ScheduledThreadPoolExecutor预留的钩子
        } finally {
            mainLock.unlock();
        }
        tryTerminate();//尝试关闭空闲线程.这个方法上面也说过了
    }
```

### shutdownNow

关闭线程池，不再接受新的任务，正在执行的任务尝试终止。

```java
  public List<Runnable> shutdownNow() {
        List<Runnable> tasks;
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            checkShutdownAccess();//权限验证
            advanceRunState(STOP);//线程池的状态置为STOP
            interruptWorkers();//强制中断所有状态不是-1的,就是所有启动了的workers.
            tasks = drainQueue();
        } finally {
            mainLock.unlock();
        }
        tryTerminate();
        return tasks;
    }
 private void interruptWorkers() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            for (Worker w : workers)//循环所有的worker
                w.interruptIfStarted();//直接执行中断
        } finally {
            mainLock.unlock();
        }
    }
 void interruptIfStarted() {
            Thread t;
            //只有刚刚构建的worker的时候,状态state值是-1(这里也能体现刚构建的worker无法被中断),其他情况都是>=0的
            if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
                try {
                    t.interrupt();
                } catch (SecurityException ignore) {
                }
            }
        }
```

### isShutdown

确认线程池是否关闭。判断状态是不是RUNNING.

```java
public boolean isShutdown() {
        return ! isRunning(ctl.get());
    }
```

### isTerminated

确认执行shutdown或者shutdownNow后，线程池是否关闭。

源码很简单就是看当前线程池的状态是不是TERMINATED

### awaitTermination

等待一段时间，如果到超时时间了，还没有terminated则返回false，反之则线程池已经terminated，返回true。

```java
 public boolean awaitTermination(long timeout, TimeUnit unit)
        throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            for (;;) {
                if (runStateAtLeast(ctl.get(), TERMINATED))//如果状态已经是TERMINATED
                    return true;
                if (nanos <= 0)
                    return false;
                nanos = termination.awaitNanos(nanos);//通知机制tryTerminate中将线程池状态设置为TERMINATED后,发通知到这里
            }
        } finally {
            mainLock.unlock();
        }
    }
```

### isTerminating

是不是正在停止中,看下面源码分析吧,一下子就明白了

```java
 public boolean isTerminating() {
        int c = ctl.get();
        return ! isRunning(c) && runStateLessThan(c, TERMINATED);//看看状态是不是TIDYING/STOP/SHUTDOWN如果是这三种状态就返回true,此时线程池正在停止,不是这三种状态并且不是RUNNING状态,那么就是TERMINATED状态,返回false,此时线程池已经终止了.
    }
 private static boolean runStateLessThan(int c, int s) {
        return c < s;
    }
```

### prestartCoreThread

启动一个空闲的线程作为核心线程,

- 如果核心线程数已到阈值, 会加入失败, 返回false, 如果线程池处于SHUTDOWN以上的状态也返回false,
- 只有真正这个线程调用start方法跑起来, 才会返回true.

```java
public boolean prestartCoreThread() {
        return workerCountOf(ctl.get()) < corePoolSize &&
            addWorker(null, true);
    }
```

### prestartAllCoreThreads

启动所有核心线程，使他们等待获取任务。

```java
public int prestartAllCoreThreads() {
        int n = 0;
        while (addWorker(null, true))//null代表空闲线程,true代表是增加的是核心线程
            ++n;//死循环增加空闲 worker 而已
        return n;
    }
```

## 个人观点输出

### 1)下面的线程池定义会有什么问题?

```java
public class MXThreadPool {
    private final static int CORE_SIZE = 80;//核心线程数
    private final static int MAX_SIZE = 900;//最大线程数
    private final static long ALIVE_TIME = 500;//超过核心线程数的其他线程存活时间
    private final static int QUEUE_SIZE = 160;//队列大小
    private static volatile ThreadPoolExecutor threadPoolExecutor;

    public static ThreadPoolExecutor getInstance(){
        if(threadPoolExecutor == null){
            synchronized (CommonThreadPool.class){
                if(threadPoolExecutor == null){
                    String poolName = "mx-common";
                    threadPoolExecutor = new ThreadPoolExecutor(
                            CORE_SIZE,
                            MAX_SIZE,
                            ALIVE_TIME,
                            TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<>(QUEUE_SIZE),
                            new CommonThreadFactory(poolName),
                            new MonitoredCallerRunsPolicy(poolName));
                }
            }
        }
        return threadPoolExecutor;
    }
}
```

先看图(从右往左看):

<img src="https://segmentfault.com/img/remote/1460000021052378" alt="img" style="zoom:150%;" />

图上描述的是当处于业务高峰的时候, 此时阻塞队列是满的, 线程总量也达到最大线程数的阈值,

- 这个时候所有的线程在执行完创建线程指定的firstTask后,都会阻塞在BlockingQueue的take或者pull方法上去,
- 此时依赖队列的通知机制了,队列中有一个每增加一个就通知一个线程, 这就有问题了, 当第一波高峰来了以后,所有线程都在等待任务, 但此时队列大小只有160, 因此,同一时刻, 只有160个线程可以被唤醒, 只有160个线程可以工作.

*文字描述确实苍白无力, 可能是我文字功底太差吧, 哈哈*

**阶段总结**

这里建议线程池的最大值设置与阻塞队列的大小保持一致, 这样所有线程都可以参与工作, 不会出现大量线程除以timed_waiting的情况.

**那么具体刚创建一个线程池的时候需要怎么指定大小呢 ?**

我的建议:

1. 所有线程池都使用自定义的,别用Executors下面的, 这个懂得人用什么都是对的, 不懂得还是自定义好一点, 方便规避问题.
2. 是根据业务场景 ,做好线程池资源的隔离, 将快满服务分别创建线程池进行管理.
3. 项目刚创建的时候，可以先按照网上的说法根据业务区分CPU密集还是IO密集型服务，定义线程数，但重点是要自定义拒绝策略，加上必要的监控指标比如getActiveCount、getLargestPoolSize等（详见实时指标），上线后根据监控指标动态调整,这是一个调优的过程.

### 2) LinkedBlockingQueue 和 ArrayBlockingQueue对比

在自定义线程池的时候,你应该使用哪种队列呢, 上google或者baidu几乎所有的文章都推荐使用LinkedBlockingQueue

我这边不这么认为, 通过看源码, 发现在构建线程池这里并指定队列大小的情况下, 使用ArrayBlockingQueue的性能会更好, 因为不涉及到队列的扩容拷贝, 也不涉及到队列中元素移动, ArrayBlockingQueue操作完全更简单, 比维护一个链表, 性能更好.

### 3) Worker为什么不使用ReentrantLock来实现呢？

- ReentrantLock是可重入的，但我们这里线程资源的控制要求是不可重入的 ！！！

### 4）在runWorker方法中，为什么要在执行任务的时候对每个工作线程都加锁呢？

- shutdown 方法与 getTask 方法存在竞态条件，其实很多地方加锁的原因都是竞态条件的原因，具体每个地方都要看源码。
- 另一种角度来看，更方便管理需要同步的场景。

### 5）为啥workers用hashSet+锁实现, 而不是使用线程安全的数据结构 ？

主要是因为这里有些复合的操作，比如说将worker添加到workers后，我们还需要判断是否需要更新largestPoolSize等，workers只在获取到mainLock的情况下才会进行读写，另外这里的mainLock也用于在中断线程的时候串行执行，否则如果不加锁的话，可能会造成并发去中断线程，引起不必要的中断风暴（大量线程去中断，大量阻塞在synchronized，进行锁竞争CPU飚高）。







