package com.lv.multithread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 有返回值的线程
 */
public class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(4000);
        return "我是一个有返回值的线程";
    }

    public static void main(String[] args) throws Exception {
        MyCallable myCallable=new MyCallable();
        FutureTask futureTask=new FutureTask(myCallable);
        Thread thread=new Thread(futureTask);
        thread.start();
        System.out.println("是否完成："+futureTask.isDone());
        System.out.println(futureTask.get());
        System.out.println(futureTask.isDone());
    }
}
