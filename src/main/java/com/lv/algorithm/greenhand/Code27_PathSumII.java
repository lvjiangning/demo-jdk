package com.lv.algorithm.greenhand;

import java.util.ArrayList;
import java.util.List;
// 测试链接：https://leetcode.com/problems/path-sum-ii
/**
 * 给定root二叉树的 和整数targetSum，返回路径中节点值之和等于 的所有从根到叶targetSum的路径。每个路径都应作为节点值列表返回，而不是节点引用。
 * 根到叶路径是从根开始到任何叶节点结束的路径。叶是没有子节点的节点。
 */
public class Code27_PathSumII {


	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		TreeNode(int val) {
			this.val = val;
		}
	}

	/**
	 *
	 * @param root 根节点
	 * @param sum 路径和
	 * @return 等于路径和的路径数组
	 */
	public static List<List<Integer>> pathSum(TreeNode root, int sum) {
		//返回结果，可能存在多条路径
		List<List<Integer>> ans = new ArrayList<>();
		if (root == null) {
			return ans;
		}
		ArrayList<Integer> path = new ArrayList<>();
		process(root, path, 0, sum, ans);
		return ans;
	}

	/**
	 *
	 * @param x 当前节点
	 * @param path  路径
	 * @param preSum 当前累加和，不能使用Integer类型，不能传递引用
	 * @param sum 路径和
	 * @param ans 返回值
	 */
	public static void process(TreeNode x, List<Integer> path, int preSum, int sum, List<List<Integer>> ans) {
		//左右节点为空表示到了叶子节点
		if (x.left == null && x.right == null) {
			if (preSum + x.val == sum) {
				//加入路径
				path.add(x.val);
				//拷贝一份新的集合到结果，引用类型不拷贝，可能会更改
				ans.add(copy(path));
				//恢复现场，回到上一级
				path.remove(path.size() - 1);
			}
			return;
		}
		// x 非叶节点
		path.add(x.val);
		//添加本级值
		preSum += x.val;
		if (x.left != null) {
			process(x.left, path, preSum, sum, ans);
		}
		if (x.right != null) {
			process(x.right, path, preSum, sum, ans);
		}
		//恢复现场 本级已经计算完成 返回上一级的路径
		path.remove(path.size() - 1);
	}

	public static List<Integer> copy(List<Integer> path) {
		List<Integer> ans = new ArrayList<>();
		for (Integer num : path) {
			ans.add(num);
		}
		return ans;
	}

}
