package com.lv.multithread.locks.lockSupport;

import java.util.concurrent.locks.LockSupport;

/**
 * 中断测试
 * interrupt起到的作用与unpark一样，会让阻塞的线程继续执行
 */
class MyThread3 extends Thread {
    private Object object;

    public MyThread3(Object object) {
        this.object = object;
    }

    public void run() {
        System.out.println("main before interrupt");
        try {
            // 休眠3s
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread thread = (Thread) object;
        // 中断线程'
        System.out.println(thread.getName());
        thread.interrupt(); //设置main线程中断
        System.out.println("main after interrupt");
    }
}

public class InterruptDemo {
    public static void main(String[] args) {
        MyThread3 myThread = new MyThread3(Thread.currentThread());
        myThread.start();
        System.out.println("main before park");
        // 获取许塞
        LockSupport.park();
        System.out.println("main after park");
    }
}

