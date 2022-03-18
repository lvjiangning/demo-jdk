package com.lv.multithread.locks.aqs;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: lvjn
 * @Date: 2022-03-18-14:19
 * @Description:
 */
public class ReentrantReadWriteLockTest {
    static volatile int a;

    public static void readA() {
        System.out.println(a);
    }

    public static void writeA() {
        a++;
    }

    public static void main(String[] args) {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        Thread readThread1 = new Thread(() -> {
            readLock.lock();
            try {
                readA();
            } finally {
                readLock.unlock();
            }

        });
        Thread readThread2 = new Thread(() -> {
            readLock.lock();
            try {
                readA();
            } finally {
                readLock.unlock();
            }
        });

        Thread writeThread = new Thread(() -> {
            writeLock.lock();
            try {
                writeA();
            } finally {
                writeLock.unlock();
            }
        });

        readThread1.start();
        readThread2.start();
        writeThread.start();
    }
}
