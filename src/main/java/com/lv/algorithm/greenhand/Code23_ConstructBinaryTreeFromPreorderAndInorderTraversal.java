package com.lv.algorithm.greenhand;

import java.util.HashMap;
// 有一棵树，先序结果是pre[L1...R1]，中序结果是in[L2...R2]
// 请建出整棵树返回头节点
// https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal
public class Code23_ConstructBinaryTreeFromPreorderAndInorderTraversal {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * 方式一、通过while循环找 数组范围内的根节点
     * @param pre
     * @param in
     * @return
     */
    public static TreeNode buildTree1(int[] pre, int[] in) {
        //树的节点长度应该是一样的
        if (pre == null || in == null || pre.length != in.length) {
            return null;
        }
        return f(pre, 0, pre.length - 1, in, 0, in.length - 1);
    }



    /**
     *  建的思路
     *   1、每次建树都先建父节点，然后得到先序左树的值的范围，得到中序左树值的范围，
     *   2、通过上一步左树值的范围，重新进入第一步进行递归分解
     * @param pre 先序数组
     * @param L1  先序数组开始下标
     * @param R1  先序数组结束下标
     * @param in 中序数组
     * @param L2 中序数组开始下标
     * @param R2  中序数组结束下标
     * @return
     */
    public static TreeNode f(int[] pre, int L1, int R1, int[] in, int L2, int R2) {
        if (L1 > R1) {
            return null;
        }
        //创建节点
        TreeNode head = new TreeNode(pre[L1]);
        if (L1 == R1) { //表示数组节点已经创建完
            return head; //直接返回
        }
        //找到中序与先序开始下标值相同的下标，就是找到当前数组中为root节点的值
        int find = L2;
        while (in[find] != pre[L1]) {
            find++;
        }
        //建左树
        head.left = f(pre, L1 + 1, L1 + find - L2, in, L2, find - 1);
        //建右树
        head.right = f(pre, L1 + find - L2 + 1, R1, in, find + 1, R2);
        return head; //返回头节点
    }
    /**
     * 方式二、先通过map保存数与下标的关系，直接在map中通过值得到下标
     * @param pre
     * @param in
     * @return
     */
    public static TreeNode buildTree2(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length) {
            return null;
        }
        HashMap<Integer, Integer> valueIndexMap = new HashMap<>();
        for (int i = 0; i < in.length; i++) {
            valueIndexMap.put(in[i], i);
        }
        return g(pre, 0, pre.length - 1, in, 0, in.length - 1, valueIndexMap);
    }

    // 有一棵树，先序结果是pre[L1...R1]，中序结果是in[L2...R2]
    // 请建出整棵树返回头节点
    public static TreeNode g(int[] pre, int L1, int R1, int[] in, int L2, int R2,
                             HashMap<Integer, Integer> valueIndexMap) {
        if (L1 > R1) {
            return null;
        }
        TreeNode head = new TreeNode(pre[L1]);
        if (L1 == R1) {
            return head;
        }
        int find = valueIndexMap.get(pre[L1]);
        head.left = g(pre, L1 + 1, L1 + find - L2, in, L2, find - 1, valueIndexMap);
        head.right = g(pre, L1 + find - L2 + 1, R1, in, find + 1, R2, valueIndexMap);
        return head;
    }
}
