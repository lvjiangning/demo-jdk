package com.lv.algorithm.greenhand;

import java.util.List;

/**
 * 两个有序链表的合并，形成一个有序链表
 * https://leetcode.com/problems/merge-two-sorted-lists/
 *
 * @author： lvjiangning
 * @Date 2021/12/30 22:14
 */
public class Code16_MergeTwoSortedLinkedList {

    // 不要提交这个类
    public static class ListNode {
        public int val;
        public ListNode next;
    }

    public static void main(String[] args) {
        ListNode l1 = createListNode(1, 2, 5, 6, 8, 9);
        ListNode l2 = createListNode(1, 3, 4, 7);
        ListNode listNode = mergeTwoLists(l1, l2);
        printListNode(listNode);
    }

    //打印节点值
    private static void printListNode(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
        System.out.println();

    }

    private static ListNode createListNode(int... value) {
        ListNode head = new ListNode();
        head.val = value[0];
        ListNode preNode = head;
        for (int i = 1; i < value.length; i++) {
            ListNode node = new ListNode();
            node.val = value[i];
            preNode.next = node;
            preNode = node;
        }
        return head;
    }

    public static ListNode mergeTwoLists(ListNode head1, ListNode head2) {
        if (head1 == null || head2 == null) {
            return head1 == null ? head2 : head1;
        }
        ListNode head = head1.val <= head2.val ? head1 : head2;
        ListNode cur1 = head.next;
        ListNode cur2 = head == head1 ? head2 : head1;
        ListNode pre = head;
        while (cur1 != null && cur2 != null) {
            if (cur1.val <= cur2.val) {
                pre.next = cur1;
                cur1 = cur1.next;
            } else {
                pre.next = cur2;
                cur2 = cur2.next;
            }
            pre = pre.next;
        }
        pre.next = cur1 != null ? cur1 : cur2;
        return head;
    }


}
