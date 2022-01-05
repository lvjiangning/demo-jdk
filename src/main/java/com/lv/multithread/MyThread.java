package com.lv.multithread;

/**
 * 相对而言，此种方式创建线程没有接口灵活，接口可以多继承
 */
public class MyThread extends Thread{
    @Override
    public void run() {
        System.out.println("继承Thread实现接口！");
    }

    public static void main(String[] args) {
        MyThread myThread=new MyThread();
        myThread.start();
    }
}
