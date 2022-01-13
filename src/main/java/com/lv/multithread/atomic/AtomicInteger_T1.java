package com.lv.multithread.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

/**
 * 原子类 AtomicInteger 测试
 */
public class AtomicInteger_T1 {
    //原子类
    //构造方法可以设置初始值
    private static AtomicInteger atomicInteger=new AtomicInteger(10);

    public static void main(String[] args) {
        //atomicInteger.accumulateAndGet 测试
       /* AccumulateAndGetTest accumulateAndGetTest1 = new AccumulateAndGetTest();
        AccumulateAndGetTest accumulateAndGetTes2 = new AccumulateAndGetTest();
        new Thread(accumulateAndGetTes2).start();
        new Thread(accumulateAndGetTest1).start();*/

        //atomicInteger.IntUnaryOperator 测试
       /* GetAndUpdateTest GetAndUpdateTest1 = new GetAndUpdateTest();
        GetAndUpdateTest GetAndUpdateTest2 = new GetAndUpdateTest();
        new Thread(GetAndUpdateTest1).start();
        new Thread(GetAndUpdateTest2).start();*/
        //得到当前值
        System.out.println(atomicInteger.get());
        //得到并累加
        System.out.println(atomicInteger.addAndGet(10));
        //得到值后进行累加，返回累加前的值
        System.out.println(atomicInteger.getAndAdd(10));
        System.out.println("atomicInteger ="+atomicInteger);
        //自减1，返回自减后的值 相当--i
        System.out.println(atomicInteger.decrementAndGet());
        System.out.println("atomicInteger ="+atomicInteger);
        //自增1，返回自增后的值 相当++i
        System.out.println(atomicInteger.incrementAndGet());
        //参数1为预期值，如果预期值等于21，则更新为第二个值，并且返回true ,否则返回返回false
        System.out.println(atomicInteger.compareAndSet(21,20));
        //参数1为预期值，如果预期值等于21，则更新为第二个值，并且返回true ,否则返回返回false,目前看源码好像与compareAndSet一致
        System.out.println(atomicInteger.weakCompareAndSet(21,20));
        //自增1 ,返回的是以前的值,相当于i++
        System.out.println(atomicInteger.getAndIncrement());
        //自增1 ,返回的是以前的值,相当于i--
        System.out.println(atomicInteger.getAndDecrement());
        System.out.println("atomicInteger ="+atomicInteger);
        //设置新值，返回旧值
        System.out.println(atomicInteger.getAndSet(10));


    }

    /**
     * 得到值，通过设定的函数接口进行计算
     */
    static class GetAndUpdateTest implements Runnable{

        @Override
        public void run() {
            //函数式接口
            IntUnaryOperator intBinaryOperator=(y)-> y*10;
            for (int i = 0; i < 3; i++) {
                //得到值后，通过intBinaryOperator 函数进行计算
                int i1 = atomicInteger.getAndUpdate(intBinaryOperator);
                System.out.println(i1);
            }
        }
    }

    /**
     * 得到值，通过设定的函数接口进行计算
     */
   static class AccumulateAndGetTest implements Runnable{

        @Override
        public void run() {
            //函数式接口
            IntBinaryOperator intBinaryOperator=(x,y)->x+y;
            for (int i = 0; i < 5; i++) {
                //得到值后，通过intBinaryOperator 函数进行计算
                int i1 = atomicInteger.accumulateAndGet(5, intBinaryOperator);
                System.out.println(i1);
            }
        }
    }
}
