package com.lv.algorithm.greenhand;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 得到二叉树每一层的从左到右，从下到上的集合
 * https://leetcode.com/problems/binary-tree-level-order-traversal-ii
 */
public class Code24_BinaryTreeLevelOrderTraversalII {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        //返回结果
        List<List<Integer>> ans = new LinkedList<>();
        if (root == null) {
            return ans;
        }
        //队列容器 存储当前层级节点
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root); //从头部节点开始
        while (!queue.isEmpty()) { //开始循环遍历所有层级，为空表示树已遍历
            int size = queue.size(); //队列中的个数，确定for循环的次数
            List<Integer> curAns = new LinkedList<>(); //当前层级的遍历结果
            for (int i = 0; i < size; i++) { // 循环层级中所有节点个数
                TreeNode curNode = queue.poll(); //出队列，并且取当前节点值
                curAns.add(curNode.val);
                if (curNode.left != null) { //当前节点的左树不为空，则当前节点左树进队列
                    queue.add(curNode.left);
                }
                if (curNode.right != null) { //当前节点右树不为空，则右树进队列
                    queue.add(curNode.right);
                }
            }
            ans.add(0, curAns); //将结果插入数组指定位置，通过此操作完成倒序
        }
        return ans;
    }
}
