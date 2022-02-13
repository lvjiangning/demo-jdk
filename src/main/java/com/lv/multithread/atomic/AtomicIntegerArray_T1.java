package com.lv.multithread.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author lvjn
 * @version 1.0
 * @description: TODO
 * @date 2022/1/24 20:10
 */
public class AtomicIntegerArray_T1 {

    public static void main(String[] args) throws InterruptedException {
        AtomicIntegerArray array = new AtomicIntegerArray(new int[] { 0, 0 });
        System.out.println(array);
        System.out.println(array.getAndAdd(1, 2));
        System.out.println(array);
    }
}
