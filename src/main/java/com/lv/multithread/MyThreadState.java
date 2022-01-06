package com.lv.multithread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 线程的状态
 */
public class MyThreadState {
    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("2: " + this.getState());

            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new MyThread();
        System.out.println("1: " + t1.getState());
        t1.start();
        t1.join();
        System.out.println("3: " + t1.getState());

        Thread t2 = new Thread(() -> {
            try {
                LockSupport.park(); //线程阻塞
                System.out.println("t2 go on!");
                TimeUnit.SECONDS.sleep(5);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t2.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("4: " + t2.getState());

        LockSupport.unpark(t2);
        TimeUnit.SECONDS.sleep(1);
        System.out.println("5: " + t2.getState());

        final Object o = new Object();
        Thread t3 = new Thread(()->{
            synchronized (o) {
                System.out.println("t3 得到了锁 o");
            }
        });

        new Thread(()-> {
            synchronized (o) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        Thread.sleep(1000);


        t3.start();

        Thread.sleep(1000);
        System.out.println("6: " + t3.getState());

    }
}
