package com.lv.multithread.atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 解决ABA问题
 */
public class AtomicStampedReference_T1 {
    private static AtomicStampedReference<Integer> atomicStampedRef =
            new AtomicStampedReference<>(1, 0);
    public static void main(String[] args){
        System.out.println(",初始值 a = " + atomicStampedRef.getReference()+"，版本号："+atomicStampedRef.getStamp());
        System.out.println(atomicStampedRef.compareAndSet(2,3,atomicStampedRef.getStamp(),atomicStampedRef.getStamp()+1));

    }

    public static void  test(){
        Thread main = new Thread(() -> {
            System.out.println("操作线程" + Thread.currentThread() +",初始值 a = " + atomicStampedRef.getReference()+"，版本号："+atomicStampedRef.getStamp());
            int stamp = atomicStampedRef.getStamp(); //获取当前标识别
            try {
                Thread.sleep(1000); //等待1秒 ，以便让干扰线程执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean isCASSuccess = atomicStampedRef.compareAndSet(1,2,stamp,stamp +1);  //此时expectedReference未发生改变，但是stamp已经被修改了,所以CAS失败
            System.out.println("操作线程" + Thread.currentThread() +",CAS操作结果: " + isCASSuccess);
        },"主操作线程");

        Thread other = new Thread(() -> {
            Thread.yield(); // 确保thread-main 优先执行
            atomicStampedRef.compareAndSet(1,2,atomicStampedRef.getStamp(),atomicStampedRef.getStamp() +1);
            System.out.println("操作线程" + Thread.currentThread() +",【increment】 ,值 = "+ atomicStampedRef.getReference()+"，版本号："+atomicStampedRef.getStamp());
            atomicStampedRef.compareAndSet(2,1,atomicStampedRef.getStamp(),atomicStampedRef.getStamp() +1);
            System.out.println("操作线程" + Thread.currentThread() +",【decrement】 ,值 = "+ atomicStampedRef.getReference()+"，版本号："+atomicStampedRef.getStamp());
        },"干扰线程");

        main.start();
        other.start();
    }

}
