package com.lv.multithread;

/**
 * 线程的打断
 */
public class InterruptThread {
    //对象
    private static Object o = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(()-> {
//            synchronized (o) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) { //抛出异常的同时它会将线程的是否请求中断标志置为false
                System.out.println("线程被打断了吗"+Thread.currentThread().isInterrupted());
                e.printStackTrace();
            }

//            }
        });

        t1.start();

        t1.interrupt();

//        Thread t2 = new Thread(()-> {
//            synchronized (o) {
//
//            }
//            System.out.println("t2 end!");
//        });
//
//        t2.start();
//
//        t2.interrupt();
    }
}
