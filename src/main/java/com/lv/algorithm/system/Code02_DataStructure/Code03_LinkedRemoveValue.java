package com.lv.algorithm.system.Code02_DataStructure;

/**
 * 单链表通过value删除指定node
 */
public class Code03_LinkedRemoveValue {
    //单列表节点类
    public static class Node {
        public int value;
        public Node next;

        public Node(int data) {
            value = data;
        }
    }

    /**
     * 删除链表中值等于value的节点
     *
     * @param head
     * @param value
     * @return
     */
    public static Node removeValue(Node head, int value) {
        if (head == null) return null;

        while (head != null) {
            if (head.value == value) {
                head = head.next;
            } else {
                break;
            }
        }
        //记录当前节点
        Node current = head;
        //前一个节点
        Node preNode = head;
        //当前节点不等于空 表示链表未结束
        while (current != null) {
            //当前节点值 等于 排除条件
            if (current.value == value) {
                //前节点跳过当前节点指向当前节点的下一个节点
                preNode.next = current.next;
            } else {
                //前节点等于当前节点
                preNode = current;

            }
            //当前节点向下移动
            current = current.next;
        }
        return head;
    }

    public static void main(String[] args) {
        Node head = new Node(5);
        Node node1 = new Node(4);
        head.next = node1;
        Node node2 = new Node(5);
        node1.next = node2;
        Node node3 = new Node(2);
        node2.next = node3;
        Node node4 = new Node(4);
        node3.next = node4;
        Node node5 = new Node(1);
        node4.next = node5;
        Node node = removeValue(head, 4);
        while (node != null) {
            System.out.println(node.value);
            node = node.next;
        }
    }

}
