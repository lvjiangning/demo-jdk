package com.lv.algorithm.greenhand;

/**
 * 判断是否是平衡二叉树
 * 左节点比根节点小，右节点比根节点大
 */
public class Code28_IsBinarySearchTree {

	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		TreeNode(int val) {
			this.val = val;
		}
	}

	public static class Info {
		public boolean isBST; //是否平衡二叉树
		public int max; //最大值
		public int min; //最小值

		public Info(boolean is, int ma, int mi) {
			isBST = is;
			max = ma;
			min = mi;
		}
	}


	public static Info process(TreeNode x) {
		if (x == null) {
			return null;
		}
		Info leftInfo = process(x.left);
		Info rightInfo = process(x.right);
		int max = x.val; //
		int min = x.val; //
		if (leftInfo != null) {
			max = Math.max(leftInfo.max, max);
			min = Math.min(leftInfo.min, min);
		}
		if (rightInfo != null) {
			max = Math.max(rightInfo.max, max);
			min = Math.min(rightInfo.min, min);
		}
		boolean isBST = false; //默认不是平衡二叉树
		boolean leftIsBst = leftInfo == null ? true : leftInfo.isBST;  //如果为空则是平衡二叉树
		boolean rightIsBst = rightInfo == null ? true : rightInfo.isBST; //如果为空则是平衡二叉树
		boolean leftMaxLessX = leftInfo == null ? true : (leftInfo.max < x.val);
		boolean rightMinMoreX = rightInfo == null ? true : (rightInfo.min > x.val);
		if (leftIsBst && rightIsBst && leftMaxLessX && rightMinMoreX) { //左树是，右树是，左树的最大值小于当前节点值，右数值大于当前节点值
			isBST = true;
		}
		return new Info(isBST, max, min);
	}

}
