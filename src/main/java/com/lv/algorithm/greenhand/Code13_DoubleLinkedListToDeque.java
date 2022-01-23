package com.lv.algorithm.greenhand;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 双链表实现双端队列
 *
 * @author： lvjiangning
 * @Date 2021/12/30 21:12
 */
public class Code13_DoubleLinkedListToDeque {
    public static class Node<V> {
        public V value;
        public Node<V> last;
        public Node<V> next;

        public Node(V v) {
            value = v;
            last = null;
            next = null;
        }
    }

    static class MyDeque<V> {
        private Node<V> head;
        private Node<V> tail;
        private int size;

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void pushHead(V value) { //往双端队列的头部添加元素
            Node<V> node = new Node<>(value);
            if (head == null) {
                head = node;
                tail = node;
            } else {
                head.last=node;
                node.next=head;
                head = node;
            }
            size++;
        }

        public void pushTail(V value) { //往双端队列的尾部添加元素
            Node<V> node = new Node<>(value);
            if (tail == null) {
                head = node;
                tail = node;
            } else {
                node.last=tail;
                tail.next=node;
                tail = node;
            }
            size++;
        }
        public V pollHead(){ //弹出头部队列，并且返回值
            V answer = null;
            if (head != null){
                answer=head.value;
                if (head == tail){
                    tail =null;
                    head =null;
                }else{
                    head=head.next; //头部向下移动一个节点
                    head.last = null;
                }
                size --;
            }
            return answer;
        }

        public V pollTail(){ //弹出尾部队列，并且返回值
            V answer = null;
            if (tail != null){
                answer=tail.value;
                if (tail == head){
                    tail = null;
                    head = null;
                }else { //上一节点指向当前弹出节点的关系要去掉
                    tail=tail.last; //头部向上移动一个节点
                    tail.next = null;
                }
                size --;
            }
            return answer;
        }

        public V peekHead(){ //弹出队列头部的值，节点不出队列
            return  head == null ? null:head.value;
        }

        public V peekTail(){ //弹出队列尾部的值，节点不出队列
            return  tail == null ? null:tail.value;
        }
    }

    public static void testDeque() {
        MyDeque<Integer> myDeque = new MyDeque<>();
        Deque<Integer> test = new LinkedList<>();
        int testTime = 1000000;
        int maxValue = 200000000;
        System.out.println("测试开始！");
        for (int i = 0; i < testTime; i++) {
            if (myDeque.isEmpty() != test.isEmpty()) {
                System.out.println("Oops!");
            }
            if (myDeque.size() != test.size()) {
                System.out.println("Oops!");
            }
            double decide = Math.random();
            if (decide < 0.33) {
                int num = (int) (Math.random() * maxValue);
                if (Math.random() < 0.5) {
                    myDeque.pushHead(num);
                    test.addFirst(num);
                } else {
                    myDeque.pushTail(num);
                    test.addLast(num);
                }
            } else if (decide < 0.66) {
                if (!myDeque.isEmpty()) {
                    int num1 = 0;
                    int num2 = 0;
                    if (Math.random() < 0.5) {
                        num1 = myDeque.pollHead();
                        num2 = test.pollFirst();
                    } else {
                        num1 = myDeque.pollTail();
                        num2 = test.pollLast();
                    }
                    if (num1 != num2) {
                        System.out.println("Oops!");
                    }
                }
            } else {
                if (!myDeque.isEmpty()) {
                    int num1 = 0;
                    int num2 = 0;
                    if (Math.random() < 0.5) {
                        num1 = myDeque.peekHead();
                        num2 = test.peekFirst();
                    } else {
                        num1 = myDeque.peekTail();
                        num2 = test.peekLast();
                    }
                    if (num1 != num2) {
                        System.out.println("Oops!");
                    }
                }
            }
        }
        if (myDeque.size() != test.size()) {
            System.out.println("Oops!");
        }
        while (!myDeque.isEmpty()) {
            int num1 = myDeque.pollHead();
            int num2 = test.pollFirst();
            if (num1 != num2) {
                System.out.println("Oops!");
            }
        }
        System.out.println("测试结束！");
    }

    public static void main(String[] args) {
        testDeque();
    }
}

