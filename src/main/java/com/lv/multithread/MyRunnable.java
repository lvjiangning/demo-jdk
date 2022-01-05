package com.lv.multithread;

public class MyRunnable implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(5000); //测试主线程结束后，此线程是否会关闭
        }catch (Exception e){

        }
        System.out.println("使用Runnable接口创建线程");
    }

    public static void main(String[] args) {
        MyRunnable myRunnable=new MyRunnable();
        Thread thread=new Thread(myRunnable);
        thread.start();
    }
}
