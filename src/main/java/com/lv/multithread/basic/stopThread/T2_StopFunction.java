package com.lv.multithread.basic.stopThread;

/**
 * 线程stop:会马上停止线程，并且释放锁，直接干掉容易出现数据不一致问题
 */
public class T2_StopFunction {
    private static  Object lock=new Object();
    public static void main(String[] args)  throws Exception{
        testStopFunction();
    }

    /**
     * 测试线程stop方法
     */
    public static void testStopFunction() throws InterruptedException {
      Thread t1=  new Thread(()->{
           System.out.println("t1  wait synchronized ");
            synchronized (lock){
                System.out.println("t1 synchronized ing ");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("t1 end");
        });


        Thread t2=  new Thread(()->{
            System.out.println("t2  wait synchronized ");
            synchronized (lock){
                System.out.println("t2 synchronized ing ");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("t2 end");
        });
        t1.start();
        // 主线程休眠1秒，保证t1正常启动
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t1.stop();



    }
}
