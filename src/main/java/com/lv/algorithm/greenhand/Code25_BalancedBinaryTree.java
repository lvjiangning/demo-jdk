package com.lv.algorithm.greenhand;

/**
 * 给定一棵二叉树，判断它是否高度平衡。
 * 对于这个问题，高度平衡的二叉树定义为：
 * 一棵二叉树，其中每个节点的左子树和右子树的高度差不超过 1。
 * 测试链接：https://leetcode.com/problems/balanced-binary-tree
 */
public class Code25_BalancedBinaryTree {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    public static class Info {
        public boolean isBalanced; //是否平衡二叉树
        public int height; //高度

        public Info(boolean i, int h) {
            isBalanced = i;
            height = h;
        }
    }

    public static boolean isBalanced(TreeNode root) {
        return process(root).isBalanced;
    }

    public static Info process(TreeNode root) {
        //如果是空节点，则是平衡二叉树
        if (root == null) {
            return new Info(true, 0);
        }
        //得到左树信息
        Info leftInfo = process(root.left);
        //得到右树信息
        Info rightInfo = process(root.right);
        //判断左右树的中大的高度 +1是表示自己的本身
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        //判断左右树是否平衡，以及层级误差小于2
        boolean isBalanced = leftInfo.isBalanced && rightInfo.isBalanced
                && Math.abs(leftInfo.height - rightInfo.height) < 2;
        return new Info(isBalanced, height); //返回当前节点结果
    }

}

