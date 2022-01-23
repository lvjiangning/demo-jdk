package com.lv.algorithm.greenhand;

import java.util.List;

/**
 * 写点注释吧
 * 给定两个链表的头节点head1和head2,
 * 认为从左到右是某个数字从低位到高位，返回相加之后的链表
 * 例子
 * 4 -> 3 -> 6
 * 2 -> 5 -> 3
 * 返回
 * 6->8->9
 * 解释
 * 634+352=986
 * 测试链接：https://leetcode.com/problems/add-two-numbers/
 *
 * @author： lvjiangning
 * @Date 2021/12/30 22:11
 */
public class Code15_AddTwoNumbers {
    // 不要提交这个类
    public static class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    //打印节点值
    private static void printListNode(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
        System.out.println();

    }

    public static void main(String[] args) {
        ListNode l1 = createListNode(8, 9, 9, 9, 9);
        ListNode l2 = createListNode(8, 9);
        ListNode node = addTwoNumbers(l1, l2);
        printListNode(node);
    }

    private static ListNode createListNode(int... value) {
        ListNode head = new ListNode(value[0]);
        ListNode preNode = head;
        for (int i = 1; i < value.length; i++) {
            ListNode node = new ListNode(value[i]);
            preNode.next = node;
            preNode = node;
        }
        return head;
    }

    public static ListNode addTwoNumbers(ListNode head1, ListNode head2) {
        //两个链表的长度
        int l1 = listLength(head1);
        int l2 = listLength(head2);
        //计算出长链表 与短链表
        ListNode curl = l1 >= l2 ? head1 : head2;
        ListNode curs = curl == head1 ? head2 : head1;
        // 保存进位信息、保存上个节点的信息
        ListNode l=curl; //返回值
        int carry = 0;
        ListNode last = curl;
        int curNum = 0; //当前结果
        //先处理短链表
        while (curs != null) {
            curNum = curs.val + curl.val + carry;
            curl.val = curNum % 10;
            carry = curNum / 10; //进位的数
            last = curl;
            curl = curl.next;
            curs = curs.next;
        }
        //再处理长链表
        while (curl != null) {
            curNum =  curl.val + carry;
            curl.val = curNum % 10;
            carry = curNum / 10; //进位的数
            last = curl;
            curl = curl.next;
        }
        //如果长链表有进位，则创建一个节点
        if (carry >0 ){
            last.next=new ListNode(carry);
        }
        return l;

    }

    // 求链表长度
    public static int listLength(ListNode head) {
        int len = 0;
        while (head != null) {
            len++;
            head = head.next;
        }
        return len;
    }
}
