package com.lv.multithread.locks.lockSupport;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport 的阻塞与通知
 */
public class MyThread extends Thread {
        private Object object;

        public MyThread(Object object) {
            this.object = object;
        }

        public void run() {
            System.out.println(" running");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 获取blocker
            System.out.println("main Blocker info " + LockSupport.getBlocker((Thread) object));
            // 释放许可，释放的是object （main）线程
            LockSupport.unpark((Thread) object);
            // 休眠500ms，保证先执行park中的setBlocker(t, null);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 再次获取blocker
            System.out.println("main Blocker info " + LockSupport.getBlocker((Thread) object));

            System.out.println("main after unpark");
        }


        public static void main(String[] args) {
            MyThread myThread = new MyThread(Thread.currentThread());
            myThread.start();
            System.out.println("main before park");
            // 获取许可,设置阻塞
            LockSupport.park("ParkAndUnparkDemo"); //阻塞的是当前线程
            //设置阻塞信息后
            System.out.println("main after park");
        }

}
