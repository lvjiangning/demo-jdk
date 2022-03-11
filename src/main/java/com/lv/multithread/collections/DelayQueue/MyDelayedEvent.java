package com.lv.multithread.collections.DelayQueue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 任务事件封装
 */
public class MyDelayedEvent implements Delayed {
    private Task task;

    private Long endTime; //毫秒
    // endTime 毫秒
    public MyDelayedEvent(Task task, Long endTime) {
        this.task = task;
        this.endTime = endTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {


        return unit.convert(endTime, TimeUnit.MILLISECONDS) - unit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     *  compareTo 通过排序决定队列的先后顺序
     * @param o
     * @return 延迟时间长的存放在最前面
     */
    @Override
    public int compareTo(Delayed o) {
        if (this == o)
            return 1;
        if (o == null)
            return -1;
        long diff = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
        return diff > 0 ? 1 : (diff == 0 ? 0 : -1);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}

