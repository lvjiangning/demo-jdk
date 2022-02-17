package com.lv.multithread.collections.DelayQueue;

/**
 * 学生任务
 */
public class StudentTask implements Task {
    private Integer student;

    @Override
    public void executeTask() {
        System.out.println("学生任务执行" + student);
    }

    public StudentTask(Integer student) {
        this.student = student;
    }

    public Integer getStudent() {
        return student;
    }

    public void setStudent(Integer student) {
        this.student = student;
    }
}
