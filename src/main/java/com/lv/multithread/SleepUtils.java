package com.lv.multithread;

/**
 * 线程休眠工具类
 */
public class SleepUtils {
    public static void sleep(int  mills){
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
