package com.lv.multithread.locks.aqs;

import java.util.concurrent.Semaphore;

/**
 * @Author: lvjn
 * @Date: 2022-03-19-8:22
 * @Description:
 */
public class SemaphoreTest {
    private static Semaphore semaphore = new Semaphore(3);

    public static void testMethod(){
        try
        {
            semaphore.acquire();

            System.out.println(Thread.currentThread().getName()+" 开始时间："+System.currentTimeMillis());
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName()+" 结束时间： "+System.currentTimeMillis());


            semaphore.release();
        }
        catch(Exception e)
        {

        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(()->{
                testMethod();
            }).start();
        }
    }

}
