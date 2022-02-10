package com.lv.multithread.basic.volatile_;

/**
 * @author lvjn
 * @version 1.0
 * @description: volatile 不能保证线程安全
 * @date 2022/1/11 21:09
 */
public class Volatile_ThreadSafe {
    public static volatile int t = 0;

    public static void main(String[] args) {

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            //每个线程对t进行1000次加1的操作
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        t++; // 或者t=t+1
                    }
                }
            });
            threads[i].start();
        }

        //等待所有累加线程都结束
        while (Thread.activeCount() > 1) {
            Thread.yield();
        }

        //打印t的值
        System.out.println(t);
    }
}
