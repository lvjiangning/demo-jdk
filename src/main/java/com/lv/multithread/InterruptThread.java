package com.lv.multithread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程的打断
 *      * public void interrupt()            //t.interrupt() 打断t线程（设置t线程某给标志位f=true，并不是打断线程的运行）
 *      * public boolean isInterrupted()     //t.isInterrupted() 查询打断标志位是否被设置（是不是曾经被打断过）
 *      * public static boolean interrupted()//Thread.interrupted() 查看“当前”线程是否被打断，如果被打断，恢复标志位
 * 1、线程正在争夺synchronized锁时，竞争线程不会中断,必须获取锁后才会有中断信号
 * 2、占有synchronized锁时，interrupt命令会使线程中断
 * 3、InterruptedException 异常会导致interrupt标志复位
 * 4、使用ReentrantLock重入锁，可以用interrupt打断，用lockInterruptibly 判断是否有打断信号
 */
public class InterruptThread {
    //对象
    private static Object o = new Object();

    public static void main(String[] args) throws Exception {

        test4();
    }
    /**
     *  线程正在等待synchronized锁时，竞争线程不会中断,必须获取锁后才会有中断信号
     */
    public static  void  test4() throws InterruptedException {
        Thread t1 = new Thread(()-> {
            try {
                System.out.println("t1 wait synchronized");
                synchronized (o){
                    System.out.println("t1 synchronized ing...");
                    Thread.sleep(5000);
                }
                System.out.println("t1 thread end");
            } catch (InterruptedException e) { //抛出异常的同时它会将线程的是否请求中断标志置为false
                e.printStackTrace();
                System.out.println("t1 thread interrupt status="+Thread.currentThread().isInterrupted());
            }
        });


        Thread t2 = new Thread(()-> {
            try {
                System.out.println("t2 wait synchronized");
                synchronized (o){
                    System.out.println("t2 synchronized ing...");
                    Thread.sleep(5000);
                }
                System.out.println("t2 thread end");
            } catch (InterruptedException e) { //抛出异常的同时它会将线程的是否请求中断标志置为false
                e.printStackTrace();
                System.out.println("t2 thread interrupt status="+Thread.currentThread().isInterrupted());
            }
        });

        t1.start(); //t1 先启动
        System.out.println("t1 thread start");
        Thread.sleep(500); //间隔1秒后线程2启动，保证线程1占用锁
        t2.start(); //t2 启动
        Thread.sleep(500); //间隔1秒后线程2发出中断信号
        t2.interrupt();

    }

    /**
     *  占有synchronized锁时，interrupt命令会使线程中断
     */
    public static  void  test3() throws InterruptedException {
        Thread t3 = new Thread(()-> {
            try {
                synchronized (o){
                    System.out.println("synchronized ing...");
                    Thread.sleep(5000);
                }
                System.out.println("t3 thread end");
            } catch (InterruptedException e) { //抛出异常的同时它会将线程的是否请求中断标志置为false
                e.printStackTrace();
                System.out.println("t3 thread interrupt status="+Thread.currentThread().isInterrupted());
            }
        });

        t3.start();
        System.out.println("t3 thread start");
        Thread.sleep(1000);
        t3.interrupt();

    }

    /**
     * 测试案例
     * InterruptedException 异常会导致interrupt标志复位
     */
     public static  void  test1(){
                 Thread t1 = new Thread(()-> {
            try {
                Thread.sleep(5000);
                System.out.println("t1 thread end");
            } catch (InterruptedException e) { //抛出异常的同时它会将线程的是否请求中断标志置为false
                e.printStackTrace();
                System.out.println("t1 thread interrupt status="+Thread.currentThread().isInterrupted());
            }
        });

        t1.start();
        System.out.println("t1 thread start");
        t1.interrupt();

     }

    /**
     * Interrupted中断测试案例
     * public void interrupt()            //t.interrupt() 打断t线程（设置t线程某给标志位f=true，并不是打断线程的运行）
     * public boolean isInterrupted()     //t.isInterrupted() 查询打断标志位是否被设置（是不是曾经被打断过）
     * public static boolean interrupted()//Thread.interrupted() 查看“当前”线程是否被打断，如果被打断，恢复标志位
     */
    public static void test2() throws Exception{
        Thread t2 = new Thread(()-> {
            while (!Thread.currentThread().isInterrupted()){ //判断线程是否被中断
                System.out.println("t2 thread run");
            }
            System.out.println("t2="+Thread.currentThread().isInterrupted() +".thread end");
            System.out.println("t2= Thread reset = "+Thread.interrupted() +".thread end");
            System.out.println("t2 Interrupted status="+Thread.currentThread().isInterrupted());
        });

        t2.start();
        System.out.println("t2 thread start");
        Thread.sleep(2000);
        t2.interrupt();
    }
    //使用ReentrantLock重入锁，可以用interrupt打断，用lockInterruptibly 判断是否有打断信号
    private static ReentrantLock lock = new ReentrantLock();
    public static void test5() throws Exception{
        Thread t1 = new Thread(()-> {
            System.out.println("t1 start!");
            lock.lock();
            try {
                System.out.println("t1 locking!");
               Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("t1 unlock!");
            }
            System.out.println("t1 end!");
        });

        Thread t2 = new Thread(()-> {
            System.out.println("t2 start!");
            try {
                lock.lockInterruptibly(); //如果有打断信号，则不竞争锁了，如果没有才继续竞争
                System.out.println("t2 locking!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("t2 unlock!");
            }
            System.out.println("t2 end!");
        });

        t1.start(); //t1先启动，间隔1秒后t2启动
        Thread.sleep(1000);
        t2.start();
        Thread.sleep(1000);
        t2.interrupt(); //对t2发出中断信号
    }

}
