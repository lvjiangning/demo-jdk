package com.lv.multithread.threadPool;

import java.util.concurrent.*;

/**
 * 优点
 * a. 重用存在的线程，减少对象创建、消亡的开销，性能佳。
 * b. 可有效控制最大并发线程数，提高系统资源的使用率，同时避免过多资源竞争，避免堵塞。
 * c. 提供定时执行、定期执行、单线程、并发数控制等功能。
 *
 * 线程工具类
 * Executor 管理多个异步任务的执行，而无需程序员显式地管理线程的生命周期。这里的异步是指多个任务的执行互不干扰，不需要进行同步操作。
 * <p>
 * CachedThreadPool: 一个任务创建一个线程；
 * FixedThreadPool: 所有任务只能使用固定大小的线程；
 * SingleThreadExecutor: 相当于大小为 1 的 FixedThreadPool。
 */
public class MyExecutor {
    /**
     * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
     */
    public static void cachedThreadPoolTest() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int index = i;

            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //打印当前的线程数
                    System.out.println(index);
                }
            });
        }
    }

    /**
     * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
     */
    public static void fixedThreadPoolTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final Integer index = i;
            System.out.println("forindex=" + index);
            executorService.execute(() -> {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("runableIndex=" + index);
            });
        }
    }

    /**
     * 创建一个定长线程池，支持定时及周期性任务执行,超出长度，则在队列中等待
     */
    public static void scheduledThreadPoolTest() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final Integer index = i;
            System.out.println("forindex=" + index);
            scheduledExecutorService.schedule(() -> {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("runableIndex=" + index);
            }, 3, TimeUnit.SECONDS); //延迟三秒执行
        }
    }

    /**
     * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
     */
    public static void singleThreadExecutorTest() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
//        cachedThreadPoolTest();
        // fixedThreadPoolTest();
        // scheduledThreadPoolTest();
         singleThreadExecutorTest();
    }


}
