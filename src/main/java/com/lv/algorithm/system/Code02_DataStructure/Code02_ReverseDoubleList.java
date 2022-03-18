package com.lv.algorithm.system.Code02_DataStructure;

import cn.hutool.core.collection.CollUtil;

import java.util.List;

/**
 *
 * 双链表的反转
 * @author： lvjiangning
 * @Date 2021/12/30 20:50
 */
public class Code02_ReverseDoubleList {


    //双链表节点类
    public static class DoubleNode {
        public int value;
        public DoubleNode prev;
        public DoubleNode next;

        public DoubleNode(int data) {
            value = data;
        }
    }

    /**
     * 生成一个双链表
     *
     * @param len   链表的长度
     * @param value 链表值的范围
     * @return
     */
    public static DoubleNode generateRandomDoubleList(int len, int value) {
        if (len <= 0 || value <= 0) {
            return null;
        }
        //随机生成一个长度
        int size = (int) (Math.random() * (len + 1));
        if (size == 0) {
            return null;
        }

        size--; //head节点自动生成，
        DoubleNode head = new DoubleNode((int) (Math.random() * (value + 1)));
        DoubleNode currentNode = head; //当前节点
        DoubleNode prevNode= null;//记录前一个节点
        while (size != 0) { //除头部节点外，其他循环生成
            DoubleNode node = new DoubleNode((int) (Math.random() * (value + 1)));
            currentNode.next = node; //下一个节点指向当前节点
            currentNode.prev = prevNode; //上个节点为当前节点
            prevNode=currentNode;
            currentNode =node; //记录前一个节点
            size--;
        }
        return head;
    }

    /**
     * 按顺序获得单列表的所有值
     *
     * @param head
     * @return
     */
    public static List<Integer> getDoubleListOriginOrder(DoubleNode head) {
        List<Integer> list = CollUtil.newArrayList();
        while (head != null) {
            list.add(head.value);
            head = head.next;
        }
        return list;
    }

    /**
     * 双链表 反转
     *
     * @param head
     * @return
     */
    public static DoubleNode reverseDoubleList(DoubleNode head) {
        if (head == null) {
            return null;
        }
        DoubleNode nextNode = null;// 正序的下一个节点
        DoubleNode preNode = null; //逆序的上一个节点
        while (head != null) {
            //先记录正序的下一个节点
            nextNode = head.next;
            //当前头 指向逆序的上个节点
            head.next = preNode;
            head.prev=nextNode;
            //更改上一个节点的连接
            preNode = head;
            //当前节点往下一位
            head = nextNode;
        }
        return preNode;
    }

    /**
     * 判断双链表的反转是否正确
     *
     * @param origin
     * @param head
     * @return
     */
    public static boolean checkLinkedListReverse(List<Integer> origin, DoubleNode head) {
        for (int i = origin.size() - 1; i >= 0; i--) {
//            System.out.println("origin ="+origin.get(i)+",node.value"+head.value);
            if (!origin.get(i).equals(head.value)) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    public static void main(String[] args) {
        int len = 50;
        int value = 100;
        int testTime = 1001;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            //双链表
            DoubleNode node1 = generateRandomDoubleList(len, value);
            List<Integer> list1 = getDoubleListOriginOrder(node1);
            node1 = reverseDoubleList(node1);
            if (!checkLinkedListReverse(list1, node1)) {
                System.out.println("Oops1!");
            }
        }
    }
}
