package com.lv.multithread.threadPool.CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author: lvjn
 * @Date: 2022-03-16-14:59
 * @Description:
 */
public class Test {
    public static void main(String[] args) throws ExecutionException, InterruptedException {






        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 两个任务都需要完成后再进行任务
     */
    public static  void testException() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFutureTask1 = CompletableFuture.runAsync(() -> System.out.println("completableFutureTask1"));

        CompletableFuture<Void> completableFutureTask2= CompletableFuture.runAsync(()->{
            System.out.println("completableFutureTask2");
            int i=1/0;
        }).exceptionally((e)->{ //这样处理可以正常处理第三个任务
            e.printStackTrace();
            System.out.println("completableFutureTask2 exception");
            return  null;
        });

        //如果异常处理不是链表式调用 就不会在执行后面的任务，这里有疑问
     /*   completableFutureTask2.exceptionally((e)->{
            e.printStackTrace();
            System.out.println("completableFutureTask2 exception");
            return  null;
        });*/

        //如果没有异常处理，没有get前不会抛出异常，原因是futureTask的返回值
//        completableFutureTask2.get();



        //completableFutureTask2 有异常所以不会执行
        completableFutureTask1.runAfterBoth(completableFutureTask2,()-> System.out.println("completableFutureTask3")).get();
    }

    /**
     * 两个任务都需要完成后再进行任务
     */
    public static  void test1() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFutureTask1 = CompletableFuture.runAsync(() -> System.out.println("completableFutureTask1"));

        CompletableFuture<Void> completableFutureTask2 = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    System.out.println("completableFutureTask2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //任务1  任务2 都需要完成后，才执行任务
       completableFutureTask1.runAfterBoth(completableFutureTask2,()-> System.out.println("completableFutureTask3")).get();
    }
}
