package com.lv.multithread.threadPool.ScheduledThreadPoolExecutor;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExecutorTest {
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
        /**
         * 创建一个周期执行的任务，第一次执行延期时间为initialDelay，
         * 之后每隔period执行一次，不等待第一次执行完成就开始计时
         */
//        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(new Date()+"===scheduleAtFixedRate");
//            }
//        },2,2, TimeUnit.SECONDS);
        /**
         * 创建一个周期执行的任务，第一次执行延期时间为initialDelay，
         * 在第一次执行完之后延迟delay后开始下一次执行
         */
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(new Date()+"===scheduleWithFixedDelay");
            }
        },0,2, TimeUnit.SECONDS);



    }
}
