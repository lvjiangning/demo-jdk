package com.lv.multithread.threadPool.futuretask;

import java.util.concurrent.*;

public class FutureTaskTest {
    private final ConcurrentMap<String, Future<String>> taskCache = new ConcurrentHashMap<>();
    public  String executionTask(final String taskName)
            throws ExecutionException, InterruptedException {
        while (true) {
            Future<String> future = taskCache.get(taskName);// 从缓存中获取任务
            if (future == null) {//不存在此任务，新构建一个任务放入缓存，并启动这个任务
                Callable<String> task = () ->{
                    System.out.println("执行的任务名是"+taskName);
                    return taskName;
                } ; // 1.2创建任务
                FutureTask<String> futureTask = new FutureTask<String>(task);
                future = taskCache.putIfAbsent(taskName, futureTask);// 尝试将任务放入缓存中
                if (future == null) {
                    future = futureTask;
                    futureTask.run();//执行任务
                }
            }
            try { //若任务在缓存中了，可以直接等待任务的完成
                return future.get();// 等待任务执行完成
            } catch (CancellationException e) {
                taskCache.remove(taskName, future);
            }
        }
    }

    public static void main(String[] args)    {
        final   FutureTaskTest taskTest = new FutureTaskTest();
        for (int i = 0; i < 7; i++) {
            int finalI = i;
            System.out.println(finalI);
            new Thread(()->{
                try {
                    taskTest.executionTask("taskName" + finalI);
                    System.out.println("taskName" + finalI);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            new Thread(()->{
                try {
                    taskTest.executionTask("taskName" + finalI);
                    System.out.println("taskName" + finalI);
                    taskTest.executionTask("taskName" + finalI);
                    System.out.println("taskName" + finalI);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}