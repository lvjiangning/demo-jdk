package com.lv.algorithm.greenhand;

/**
 * 得到树的最大深度
 * 测试链接：https://leetcode.com/problems/maximum-depth-of-binary-tree
 */
public class Code22_MaximumDepthOfBinaryTree {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }

    // 以root为头的树，最大高度是多少返回！
    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        //先得到左树深度，然后再得到右数深度，然后进行判断深度+1
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }
}
