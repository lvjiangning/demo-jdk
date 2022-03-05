package com.lv.multithread.threadPool.ThreadPoolExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {
    public static void main(String[] args) throws Exception {
        ThreadGroup threadGroup=new ThreadGroup("第一个线程组");
        ThreadPoolExecutor pool=    new ThreadPoolExecutor(5, 10, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutorFactory(threadGroup),
                new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("线程池队列已满");
                System.out.println("当前任务数"+executor.getQueue().size());
                System.out.println("当前已处理任务数"+executor.getTaskCount());
                System.out.println("当前work数"+executor.getPoolSize());

            }
        });

        for (int i = 0; i < 200 ; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("====== 线程开始执行===# 当前线程名 "+Thread.currentThread().getName());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("====== 线程已执行完成===# 当前线程名 "+Thread.currentThread().getName());
                }
            });
        }
        System.out.println("#############当前已处理任务数"+pool.getTaskCount());
        System.out.println("#############当前work数"+pool.getPoolSize());
        //关闭线程池，拒绝接收先任务，但是会执行完队列中的任务
        pool.shutdown();
        //结束线程，如果在规定内未结束，则不关闭
        pool.awaitTermination(3,TimeUnit.SECONDS);
    }
}
