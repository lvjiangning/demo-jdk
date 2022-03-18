

## ReentrantLock 原理

1、默认为非公平锁，非公平锁比公平锁性能好

2、锁状态，state>=1 表示上锁, >1 表示多个线程多次获取锁，等于0 表示当前未有其他线程获得锁

### 概念

基于AQS实现的可重入锁实现类。

### 核心变量和构造器

```java
public class ReentrantLock implements Lock, java.io.Serializable {
    private final Sync sync;
    public ReentrantLock() {
        // 默认为非公平锁。为何默认为非公平锁？因为通过大量测试下来，发现非公平锁的性能优于公平锁
        sync = new NonfairSync();
    }
    public ReentrantLock(boolean fair) {
        // 由fair变量来表明选择锁类型
        sync = fair ? new FairSync() : new NonfairSync();
    }
  
}
```

## Sync内部类

```java
  abstract static class Sync extends AbstractQueuedSynchronizer {
        abstract void lock();
        // 非公平锁标准获取锁方法
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            // 当执行到这里时，正好获取所得线程释放了锁，那么可以尝试抢锁
            if (c == 0) {
                // 继续抢锁，不看有没有线程排队
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current); //如果抢锁成功，设置当前线程独占用锁
                    return true;
                }
            }
            // 当前线程就是持有锁的线程，表明锁重入
            else if (current == getExclusiveOwnerThread()) {
                // 利用state整形变量进行次数记录
                int nextc = c + acquires;
                // 如果超过了int表示范围，表明符号溢出，所以抛出异常0111 1111 + 1 = 1000 0000 
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            // 返回false 表明需要AQS来将当前线程放入阻塞队列，然后进行阻塞操作等待唤醒获取锁
            return false;
        }

        // 公平锁和非公平锁公用方法，因为在释放锁的时候，并不区分是否公平
        protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            // 如果当前线程不是上锁的那个线程
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            // 不是重入锁，那么当前线程一定是释放锁了，然后我们把当前AQS用于保存当前锁对象的变量ExclusiveOwnerThread设置为null，表明释放锁成功
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            // 注意：此时state全局变量没有改变，也就意味着在setState之前，没有别的线程能够获取锁，这时保证了以上的操作原子性
            setState(c);
            // 告诉AQS，我当前释放锁成功了，你可以去唤醒正在等待锁的线程了
            return free;
        }

        protected final boolean isHeldExclusively() {
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }

    }
```



## 公平锁

```java
 static final class FairSync extends Sync {
        // 由ReentrantLock调用
        final void lock() {
            // 没有尝试抢锁，直接进入AQS标准获取锁流程
            acquire(1);
        }
        
        // AQS调用，子类自己实现获取锁的流程
        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            // 此时有可能正好获取锁的线程释放了锁，也有可能本身就没有线程获取锁
            if (c == 0) {
                // 注意：这里和非公平锁的区别在于：hasQueuedPredecessors看看队列中是否有线程正在排队，没有的话再通过CAS抢锁
                if (!hasQueuedPredecessors() &&
                    compareAndSetState(0, acquires)) {
                    // 抢锁成功
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            // 当前线程就是获取锁的线程，那么这里是锁重入，和非公平锁操作一模一样
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            // 返回false 表明需要AQS来将当前线程放入阻塞队列，然后进行阻塞操作等待唤醒获取锁
            return false;
        }
    }
```



## 非公平锁

```java
   static final class NonfairSync extends Sync {
        // 由ReentrantLock调用获取锁
        final void lock() {
            // 非公平锁，直接抢锁，不管有没有线程排队
            if (compareAndSetState(0, 1))
                // 上锁成功，那么标识当前线程为获取锁的线程
                setExclusiveOwnerThread(Thread.currentThread());
            else
                // 抢锁失败，进入AQS的标准获取锁流程
                acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            // 使用父类提供的获取非公平锁的方法来获取锁
            return nonfairTryAcquire(acquires);
        }
    }
```



#### 核心方法

1. 获取锁操作

   ```java
   public void lock() {
       // 直接通过sync同步器上锁
       sync.lock();
   }
   
    final void lock() {
          acquire(1);
      }
   ```

2. 释放锁操作

   ```java
   public void unlock() {
       sync.release(1);
   }

#### 





#### 

