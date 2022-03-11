package com.lv.multithread.collections.DelayQueue;

/**
 * 测试类
 */
public class Test  {
    public static void main(String[] args) throws Exception {

       Thread thread=new Thread(()->{
           //创建学生任务，学生继承了task接口
           StudentTask studentTask=new StudentTask(1);
           // 创建延迟任务事件
           MyDelayedEvent myDelayedEvent1=new MyDelayedEvent(studentTask,System.currentTimeMillis()+2000); // 2秒

           StudentTask studentTask1=new StudentTask(2);
           MyDelayedEvent myDelayedEvent2=new MyDelayedEvent(studentTask1,System.currentTimeMillis()+4000);

           StudentTask studentTask2=new StudentTask(3);
           MyDelayedEvent myDelayedEvent3=new MyDelayedEvent(studentTask2,System.currentTimeMillis()+6000);

           //启动任务执行器
           MyDelayedServiceImp myDelayedServiceImp=new MyDelayedServiceImp();
           myDelayedServiceImp.init(); //启动（创建一个线程，开始消费DelayQueue）
           try {
               Thread.sleep(100);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

           myDelayedServiceImp.put(myDelayedEvent1);
           myDelayedServiceImp.put(myDelayedEvent2);
           myDelayedServiceImp.put(myDelayedEvent3);
       });
       thread.start();
        Thread.sleep(50000);

    }

}
