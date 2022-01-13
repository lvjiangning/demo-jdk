package com.lv.multithread.basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程常用方法
 */
public class MyCommonFunction {

    private Object object = new Object();

    public static void main(String[] args) {
        //testSleep();
        //testYield();
        //  testJoin();
        waitNotifyExample();
       // conditionExample();
    }

    /*Sleep,意思就是睡眠，当前线程暂停一段时间让给别的线程去运行。Sleep是怎么复活的？由你的睡眠时间而定，等睡眠到规定的时间自动复活*/
    static void testSleep() {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("A" + i);
                try {
                    Thread.sleep(500);
                    //TimeUnit.Milliseconds.sleep(500)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*Yield,就是当前线程正在执行的时候停止下来进入等待队列（就绪状态，CPU依然有可能把这个线程拿出来运行），回到等待队列里在系统的调度算法里头呢还是依然有可能把你刚回去的这个线程拿回来继续执行，当然，更大的可能性是把原来等待的那些拿出一个来执行，所以yield的意思是我让出一下CPU，后面你们能不能抢到那我不管*/
    static void testYield() {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("A" + i);
                if (i % 10 == 0) Thread.yield();


            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("------------B" + i);
                if (i % 10 == 0) Thread.yield();
            }
        }).start();
    }

    /*join， 意思就是在自己当前线程加入你调用Join的线程（），本线程等待。等调用的线程运行完了，自己再去执行。
     t1和t2两个线程，在t2的某个点上调用了t1.join,它会跑到t1去运行，t2等待t1运行完毕继续t2运行（自己join自己没有意义）
     */
    static void testJoin() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("A" + i);
                try {
                    Thread.sleep(500);
                    //TimeUnit.Milliseconds.sleep(500)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 10; i++) {
                System.out.println("B" + i);
                try {
                    Thread.sleep(500);
                    //TimeUnit.Milliseconds.sleep(500)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
        try {
            Thread.sleep(1000);
            t2.interrupt(); //t2 中断后，join的作用会失效
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * wait and notify //要用锁的对象进行操作
     * wait ：等待时不会释放锁
     */
    public static void waitNotifyExample() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        MyCommonFunction example = new MyCommonFunction();
        executorService.execute(() -> example.after());
        executorService.execute(() -> example.before());
    }


    public void before() {
        synchronized (object) {
            System.out.println("before");
//            object.notifyAll(); //要用锁的对象进行操作
        }
    }

    public void after() {
        try {
            synchronized (object) {
                object.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("after");
    }

        private ReentrantLock lock=new ReentrantLock();
        private Condition condition=lock.newCondition();
        /**
         * wait and notify
         */
        public static void conditionExample() {
            ExecutorService executorService = Executors.newCachedThreadPool();
            MyCommonFunction example = new MyCommonFunction();
            executorService.execute(() -> example.conditionAfter());
            executorService.execute(() -> example.conditionBefore());
        }

    public void conditionBefore() {
            try {
                lock.lock();
                System.out.println("before");
                condition.signal();
            }finally {
                lock.unlock();
            }

    }

    public void conditionAfter() {
        try {
            lock.lock();
            condition.await();
            System.out.println("after");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}
