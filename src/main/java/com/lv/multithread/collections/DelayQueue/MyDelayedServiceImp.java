package com.lv.multithread.collections.DelayQueue;

import java.text.SimpleDateFormat;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 服务执行者
 */
public class MyDelayedServiceImp implements MyDelayedService {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DelayQueue<MyDelayedEvent> queue = new DelayQueue<>();
    private Executor executor = Executors.newFixedThreadPool(30);//线程池,保证同一时刻执行的任务能执行s
    private Thread damon;//守护线程

    @Override
    public void init() {
        System.out.println("初始化学生守护线程");
        damon = new Thread(() -> execute()); //新建一个线程，执行execute方法
        damon.setDaemon(true);  //设置为守护线程
        damon.setName("student queue thread");  //线程名称
        damon.start();  //启动线程
    }

    @Override
    public void put(MyDelayedEvent delayed) {
        System.out.println("插入任务");
        queue.put(delayed);
    }

    @Override
    public boolean remove(MyDelayedEvent delayed) {
        System.out.println("移除任务");
        return queue.remove(delayed);
    }

    private void execute() {
        while (true) {
            //该线程要执行的内容
            try {
                MyDelayedEvent delayed = queue.take();
                if (delayed != null) {
                    System.out.println("执行任务,任务执行时当前时间是 {" + TIME_FORMAT.format(delayed.getEndTime()) + "}");
                    executor.execute(new Runnable() {
                        //将执行的任务放入线程池，同一个时刻可能有多个任务要执行
                        @Override
                        public void run() {
                            delayed.getTask().executeTask();//执行任务
                        }
                    });

                }
            } catch (InterruptedException e) {
                System.out.println("任务调度被中断");
            }
        }
    }
}
