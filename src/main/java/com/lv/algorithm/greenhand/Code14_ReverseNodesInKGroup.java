package com.lv.algorithm.greenhand;

import java.util.List;

/**
 * 写点注释吧
 * 测试链接：https://leetcode.com/problems/reverse-nodes-in-k-group/
 *
 * @author： lvjiangning
 * @Date 2021/12/30 21:41
 */
public class Code14_ReverseNodesInKGroup {
    //=============  不用提交 到 leetcode==========
    public static class ListNode {
        public int val;
        public ListNode next;

    }

    public static void main(String[] args) {
        ListNode head = new ListNode();
        head.val = 0;
        ListNode lastNode = head;
        for (int i = 1; i < 10; i++) {
            ListNode node = new ListNode();
            node.val = i;
            lastNode.next = node;
            lastNode = node;
        }
        printListNode(head);
        printListNode(reverseKGroup(head, 3));
    }

    //打印节点值
    private static void printListNode(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
        System.out.println();

    }

    //============ 不用提交到 leetCodeEnd====
    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode start = head; // k个开始节点
        ListNode end = getKGroupEnd(start, k); //得到尾部节点
        if (end == null) {
            return head; //凑不齐，返回原来的值
        }
        head = end;//返回的头部
        reverse(start, end); //头部的一轮
        ListNode lastEndNode = start;//上一组的结束节点
        while (lastEndNode.next != null) { //上一轮结束节点后还有节点
            start = lastEndNode.next;//重置开始节点
            end = getKGroupEnd(start, k); //本轮的结束节点
            if (end == null) { //null ，表示剩余节点数比k小
                return head;
            }
            reverse(start, end); //反转，头部变尾部，尾部变头部
            lastEndNode.next = end;//上一轮的尾部要与本次的头部关联
            lastEndNode = start;
        }

        return head;


    }

    /**
     * 反转链表开始节点与结束节点之间的关系
     *
     * @param
     */
    public static void reverse(ListNode start, ListNode end) {
        end = end.next; //结尾的节点
        ListNode cur = start;//当前节点
        ListNode pre = null;//上一个节点
        ListNode next = null;//下一个节点
        while (cur != end) {
            next = cur.next; //记录好当前节点的下一个节点
            cur.next = pre;
            pre = cur;
            cur = next;

        }
        start.next = end;

    }

    /**
     * 得到包含从开始节点算k个节点
     *
     * @param start
     * @param k
     * @return 如果是null 则凑不齐K个数
     */
    public static ListNode getKGroupEnd(ListNode start, int k) {
        while (--k != 0 && start != null) { //start节点算 k范围内的一个，所以先--k
            start = start.next;
        }
        return start;
    }
}
