package com.lv.algorithm.greenhand;

/**
 * 对称树
 * https://leetcode.com/problems/symmetric-tree
 */
public class Code21_SymmetricTree {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }

    public static boolean isSymmetric(TreeNode root) {
        return isMirror(root, root);
    }

    public static boolean isMirror(TreeNode h1, TreeNode h2) {
        if (h1 == null ^ h2 == null) { // ^ 相同为0 不同为1
            return false;
        }
        if (h1 == null && h2 == null) {
            return true;
        }
        //节点相等，然后再进行两边节点的判断
        return h1.val == h2.val && isMirror(h1.left, h2.right) && isMirror(h1.right, h2.left);
    }
}
