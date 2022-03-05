package com.lv.multithread.threadPool.ThreadPoolExecutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池
 */
public class ThreadPoolExecutorFactory implements ThreadFactory {
    private  ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private  String namePrefix;


    public ThreadPoolExecutorFactory(ThreadGroup group){
        this.namePrefix = "pool-" +
                group.getName() +
                "-thread-";
        this.group = group;
    }


    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon()) // 守护线程
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY) //优先级
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
