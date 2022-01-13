package com.lv.multithread.basic.stopThread;

import com.lv.multithread.SleepUtils;

/**
 * Suspend()暂停线程
 * resume() 重启线程
 */
public class T3_Suspend_Resume {
    public static void main(String[] args) {
        test_Suspend_Resume();
    }

   public static void test_Suspend_Resume(){
       Thread t1=new Thread(()->{
           for (int i = 0; i < 100; i++) {
               SleepUtils.sleep(100);
               System.out.println("t1 thread "+ i);
           }
       });
       t1.start();
       SleepUtils.sleep(1000);
       t1.suspend();
       for (int i = 0; i < 5; i++) {
           System.out.println("suspend ing "+i);
       }
       t1.resume();

   }
}
