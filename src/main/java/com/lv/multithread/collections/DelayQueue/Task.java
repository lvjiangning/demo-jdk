package com.lv.multithread.collections.DelayQueue;

/**
 * 要执行的任务
 */
public interface Task {
    //调用该方法，则会执行任务
     void executeTask();
}
