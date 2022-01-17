package com.lv.multithread.atomic;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * 也可以解决ABA问题
 * 与AtomicStampedReference不同的是，不是通过int类型的版本号，而是通过boolean类型判断
 */
public class AtomicMarkableReference_T1 {
    static AtomicMarkableReference<String> atomicMarkableReference=new AtomicMarkableReference<>("张三",true);
    public static void main(String[] args) {

    }
}
