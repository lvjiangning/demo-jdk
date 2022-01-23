package com.lv.algorithm.greenhand;


import java.util.Stack;

/**
 * 单链表实现栈
 * 先进后出
 *
 * @author： lvjiangning
 * @Date 2021/12/30 20:51
 */
public class Code12_02_LinkedListToStack {
    public static class Node<V> {
        public V value;
        public Node<V> next;

        public Node(V v) {
            value = v;
            next = null;
        }
    }

    /**
     * 测试单例表的栈
     */
    public static void testStack() {
        MyStack<Integer> myStack = new MyStack<>();
        Stack<Integer> test = new Stack<>();
        int testTime = 5000000;
        int maxValue = 200000000;
        System.out.println("测试开始！");
        for (int i = 0; i < testTime; i++) {
            if (myStack.isEmpty() != test.isEmpty()) {
                System.out.println("Oops!");
            }
            if (myStack.size() != test.size()) {
                System.out.println("Oops!");
            }
            double decide = Math.random();
            if (decide < 0.33) {
                int num = (int) (Math.random() * maxValue);
                myStack.push(num);
                test.push(num);
            } else if (decide < 0.66) {
                if (!myStack.isEmpty()) {
                    int num1 = myStack.pop();
                    int num2 = test.pop();
                    if (num1 != num2) {
                        System.out.println("Oops!");
                    }
                }
            } else {
                if (!myStack.isEmpty()) {
                    int num1 = myStack.peek();
                    int num2 = test.peek();
                    if (num1 != num2) {
                        System.out.println("Oops!");
                    }
                }
            }
        }
        if (myStack.size() != test.size()) {
            System.out.println("Oops!");
        }
        while (!myStack.isEmpty()) {
            int num1 = myStack.pop();
            int num2 = test.pop();
            if (num1 != num2) {
                System.out.println("Oops!");
            }
        }
        System.out.println("测试结束！");
    }

    public static void main(String[] args) {
        testStack();
    }

    static class MyStack<V> {
        private Node<V> head; //栈顶
        private int size;// 栈的深度

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void push(V value) { //向栈顶添加元素
            Node<V> node = new Node<>(value);
            if (head == null) {
                head = node;
            } else {
                Node temp = head;
                head = node;
                node.next = temp;
            }
            size++;
        }

        public V pop() { //出栈，并且返回值
            V answer = null;
            if (head != null) {
                answer = head.value;
                head = head.next;
                size--;
            }
            return answer;
        }

        public V peek() {//弹出头部值
            return head == null ? null : head.value;
        }

    }

}
