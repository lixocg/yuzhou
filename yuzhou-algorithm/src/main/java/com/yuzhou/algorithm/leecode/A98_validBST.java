package com.yuzhou.algorithm.leecode;

/**
 * 是否是一颗BST
 */
public class A98_validBST {

    public static boolean isValidBST(TreeNode root) {
        if(root == null){
            return true;
        }
        if(root.left == null && root.right == null){
            return true;
        }
        return isValid(root,Long.MIN_VALUE,Long.MAX_VALUE);
    }

    public static boolean isValid(TreeNode node,long min,long max){
        if(node == null){
            return true;
        }

        if( node.val <= min || node.val >= max){
            return false;
        }

        return isValid(node.left,min,node.val) &&
                isValid(node.right,node.val,max);
    }

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}

