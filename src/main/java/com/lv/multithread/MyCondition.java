package com.lv.multithread;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition类测试，消费者，生产者
 */
public class MyCondition {
        private int queueSize = 10;
        private PriorityQueue<Integer> queue = new PriorityQueue<Integer>(queueSize);
        private Lock lock = new ReentrantLock();
        private Condition notFull = lock.newCondition(); //没有满的条件
        private Condition notEmpty = lock.newCondition(); //不是空的

        public static void main(String[] args) throws InterruptedException  {
            MyCondition test = new MyCondition();
            Producer producer = test.new Producer();
            Consumer consumer = test.new Consumer();
            producer.start();
            consumer.start();
            Thread.sleep(0);
            producer.interrupt();
            consumer.interrupt();
        }

        class Consumer extends Thread{
            @Override
            public void run() {
                consume();
            }
            volatile boolean flag=true; //线程可见性，禁止重排序，通过jvm内存屏障
            private void consume() {
                while(flag){
                    lock.lock();
                  //  System.out.println("consume locking");

                    try {
                        while(queue.isEmpty()){
                            try {
                                System.out.println("队列空，等待数据");
                                notEmpty.await();
                            } catch (InterruptedException e) {
                                System.out.println("consume Interrupted");
                                flag =false;
                            }
                        }
                        queue.poll();                //每次移走队首元素
                        notFull.signal();
                        System.out.println("从队列取走一个元素，队列剩余"+queue.size()+"个元素");
                    } finally{
                        lock.unlock();
                      //  System.out.println("consume unlock");
                    }
                }
            }
        }

        class Producer extends Thread{
            @Override
            public void run() {
                produce();
            }
            volatile boolean flag=true;
            private void produce() {
                while(flag){ //如果队列没有满，则循环队列新增新元素
                    lock.lock();
                    System.out.println("produce locking");
                    try {
                        while(queue.size() == queueSize){  //队列满就等待
                            try {
                                System.out.println("队列满，等待有空余空间");
                                notFull.await();
                            } catch (InterruptedException e) {
                                System.out.println("produce Interrupted");
                                flag =false;
                            }
                        }
                        queue.offer(1);        //每次插入一个元素
                        notEmpty.signal(); //通知消费者
                        System.out.println("向队列取中插入一个元素，队列剩余空间："+(queueSize-queue.size()));
                    } finally{
                        lock.unlock();
                        System.out.println("Producer unlock");
                    }
                }
            }
        }
    }

