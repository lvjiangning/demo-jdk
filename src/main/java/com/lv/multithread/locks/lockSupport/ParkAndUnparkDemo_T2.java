package com.lv.multithread.locks.lockSupport;

import java.util.concurrent.locks.LockSupport;

/**
 * unpark在前先设置许可后，再发送park指令，许可依旧有效，而notify和wait则不行
 */
 class MyThreadT2 extends Thread {

    private Object object;

    public MyThreadT2(Object object) {
        this.object = object;
    }

    public void run() {
        System.out.println("main before unpark");
        // 释放许可
        LockSupport.unpark((Thread) object);
        System.out.println(" main after unpark");
    }
}

public class ParkAndUnparkDemo_T2 {
    public static void main(String[] args) {
        MyThreadT2 myThread = new MyThreadT2(Thread.currentThread());
        myThread.start();
        try {
            // 主线程睡眠3s
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main before park");
        // 获取许可
        LockSupport.park("ParkAndUnparkDemo");
        System.out.println("main after park");
    }
}
