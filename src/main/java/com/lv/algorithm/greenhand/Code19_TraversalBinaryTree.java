package com.lv.algorithm.greenhand;

/**
 * 遍历二叉树
 * 先序：头左右
 * 中序：左头右
 * 后序：左右头
 */
public class Code19_TraversalBinaryTree {
    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int v) {
            value = v;
        }
    }

    /**
     *  先序：头左右
     * @param head
     */
    public static void pre(Node head) {
        if (head == null) {
            return;
        }

    }

    public static void in(Node head) {
        if (head == null) {
            return;
        }

    }

    public static void pos(Node head) {

    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);

        pre(head);
        System.out.println("========");
        in(head);
        System.out.println("========");
        pos(head);
        System.out.println("========");

    }

}
