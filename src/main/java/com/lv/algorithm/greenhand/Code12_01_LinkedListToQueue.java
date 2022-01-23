package com.lv.algorithm.greenhand;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 单链表实现队列
 * 先进先出
 * @author： lvjiangning
 * @Date 2021/12/30 20:51
 */
public class Code12_01_LinkedListToQueue {

    public static class Node<V> {
        public V value;
        public Node<V> next;

        public Node(V v) {
            value = v;
            next = null;
        }
    }

    /**
     * 队列  先进先出
     * @param <V>
     */
    public static class MyQueue<V>{
        private Node<V> head; //头部
        private Node<V> tail; //尾部
        private int size; //队列的大小

        private MyQueue(){
            head=null;
            tail=null;
            size=0;
        }

        public  boolean isEmpty(){
            return  size == 0;
        }

        public  int size(){
            return size;
        }

        /**
         * 进队列
         * @param value
         */
        public void  offer(V value){
            Node<V> vNode=new Node<V>(value);
            if (tail == null){ //如果尾部节点为空，则目前是空队列，头尾指向同一个节点
                head =vNode;
                tail= vNode;
            }else {
                tail.next=vNode;
                tail=vNode;
            }
            size ++ ;
        }

        /**
         *  获取队列头部值，并且删除当前节点
         * @return
         */
        public V poll(){
            V answer=null;
            if (head !=null){
                answer=head.value;
                head=head.next;
                size --;

            }
            if (head == null){ //如果下一个节点不存在，则尾部也要指向空
                tail = null;
            }
            return answer;
        }

        public V peek() {
            V ans = null;
            if (head != null) {
                ans = head.value;
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        testQueue();
    }

    /**
     * 测试队列
     */
    public static void  testQueue(){
        MyQueue<Integer> myQueue=new MyQueue<>();
        Queue<Integer> test=new LinkedList<>(); //使用jdk的队列进行代码校验
        int testTime = 5000000;
        int maxValue = 200000000;
        System.out.println("测试开始！");
        for (int i = 0; i < testTime; i++) {
            if (myQueue.isEmpty() != test.isEmpty()) {
                System.out.println("Oops!");
            }
            if (myQueue.size() != test.size()) {
                System.out.println("Oops!");
            }
            double decide = Math.random(); //自动生成几率
            if (decide < 0.33) { // [0,0.33)，做进队列的操作
                int num = (int) (Math.random() * maxValue);
                myQueue.offer(num);
                test.offer(num);
            } else if (decide < 0.66) { //[0.33,0.66) //做值出栈的操作
                if (!myQueue.isEmpty()) {
                    int num1 = myQueue.poll();
                    int num2 = test.poll();
                    if (num1 != num2) {
                        System.out.println("Oops!");
                    }
                }
            } else { //其他做对比头部值的操作
                if (!myQueue.isEmpty()) {
                    int num1 = myQueue.peek();
                    int num2 = test.peek();
                    if (num1 != num2) {
                        System.out.println("Oops!");
                    }
                }
            }

            if (myQueue.size() != test.size()) {
                System.out.println("Oops!");
            }
            while (!myQueue.isEmpty()) {
                int num1 = myQueue.poll();
                int num2 = test.poll();
                if (num1 != num2) {
                    System.out.println("Oops!");
                }
            }

        }
        System.out.println("测试结束！");
    }
}
