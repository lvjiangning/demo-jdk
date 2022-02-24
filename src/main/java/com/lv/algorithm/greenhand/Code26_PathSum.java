package com.lv.algorithm.greenhand;
// 测试链接：https://leetcode.com/problems/path-sum

/**
 * 给定root一个二叉树和一个整数targetSum，如果树有一个从根到叶的路径，使得沿路径的所有值相加等于 ，则返回true。
 * 叶是没有子节点的节点。
 */
public class Code26_PathSum {


	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		TreeNode(int val) {
			this.val = val;
		}
	}

	public static boolean isSum = false;

	/**
	 * 是否有路径总和
	 * @param root
	 * @param sum
	 * @return
	 */
	public static boolean hasPathSum(TreeNode root, int sum) {
		if (root == null) {
			return false;
		}
		//全局变量默认false
		isSum = false;
		process(root, 0, sum);
		return isSum;
	}

	/**
	 *
	 * @param x 当前节点
	 * @param preSum  当前节点之前的累加和
	 * @param sum 要判断根节点到叶子节点的总和数
	 */
	public static void process(TreeNode x, int preSum, int sum) {
		//左右树为空表示当前节点为叶子节点
		if (x.left == null && x.right == null) {
			//累加和+当前节点值
			if (x.val + preSum == sum) {
				isSum = true;
			}
			return;
		}
		// x是非叶节点
		preSum += x.val;
		//左树不为空 递归左树
		if (x.left != null) {
			process(x.left, preSum, sum);
		}
		//右树不为空 递归右树
		if (x.right != null) {
			process(x.right, preSum, sum);
		}
	}

//	public static boolean hasPathSum(TreeNode root, int sum) {
//		if (root == null) {
//			return false;
//		}
//		return process(root, sum);
//	}
//
//	public static boolean process(TreeNode root, int rest) {
//		if (root.left == null && root.right == null) {
//			return root.val == rest;
//		}
//		boolean ans = root.left != null ? process(root.left, rest - root.val) : false;
//		ans |= root.right != null ? process(root.right, rest - root.val) : false;
//		return ans;
//	}

}
