package com.lv.algorithm.greenhand;
/**
 * 判断两个树的结构与值是否相同
 */
public class Code20_SameTree {

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }

    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null ^ q == null) { //^ 值相同为0（false） 不同为1(true)
            return false;
        }
        if (p == null && q == null) { //值都为空则为true
            return true;
        }
        // 递归遍历，头节点的值与 左右节点的值是否一致
        return p.val == q.val && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
}
