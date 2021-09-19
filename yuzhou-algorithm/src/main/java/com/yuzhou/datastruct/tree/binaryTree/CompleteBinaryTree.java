package com.yuzhou.datastruct.tree.binaryTree;


import com.yuzhou.datastruct.queue.LinkedQueue;
import com.yuzhou.datastruct.TNode;

/**
 * 利用层次遍历原理构造完全二叉树
 */
public class CompleteBinaryTree<T extends Comparable<T>> extends BinarySearchTree<T> {


    /**
     * 构建空完全二叉树
     */
    public CompleteBinaryTree() {
        super();
    }

    /**
     * 以层序遍历构造完全二叉树
     *
     * @param levelOrderArray
     */
    public CompleteBinaryTree(T[] levelOrderArray) {
        this.root = create(levelOrderArray, 0);
    }

    /**
     * 层次遍历构造完全二叉树
     *
     * @param levelOrderArray
     * @param i
     * @return
     */
    public TNode<T> create(T[] levelOrderArray, int i) {

        if (levelOrderArray == null) {
            throw new RuntimeException("the param 'array' of create method can\'t be null !");
        }
        TNode<T> p = null;

        if (i < levelOrderArray.length) {//递归结束条件
            p = new TNode<>(null, null,levelOrderArray[i]);
            p.left = create(levelOrderArray, 2 * i + 1);  //根据完全二叉树的性质 2*i+1 为左孩子结点
            p.right = create(levelOrderArray, 2 * i + 2); //2*i+2 为右孩子结点
        }

        return p;
    }
    //
    //

    /**
     * 搜索二叉树的包含操作和移除操作不适合层次遍历构造的完全二叉树
     * 根据层次遍历构建的二叉树必须用层次遍历来判断(仅适用层次遍历构建的完全二叉树)
     *
     * @param data
     * @return
     */
    @Override
    public boolean contains(T data) {
        /**
         * 存放需要遍历的结点,左结点一定优先右节点遍历
         */
        LinkedQueue<TNode<T>> queue = new LinkedQueue<>();
        StringBuffer sb = new StringBuffer();
        TNode<T> p = this.root;

        while (p != null) {

            //判断是否存在data
            if (data.compareTo(p.data) == 0) {
                return true;
            }

            //先按层次遍历结点,左结点一定在右结点之前访问
            if (p.left != null) {
                //孩子结点入队
                queue.add(p.left);
            }

            if (p.right != null) {
                queue.add(p.right);
            }
            //访问下一个结点
            p = queue.poll();
        }

        return false;
    }


    /**
     * 搜索二叉树的包含操作和移除操作不适合层次遍历构造的完全二叉树
     *
     * @param data
     * @return
     */
    @Override
    public void remove(T data) {
        //do nothing 取消删除操作
    }

    /**
     * 完全二叉树只通过层次遍历来构建,取消insert操作
     *
     * @param data
     */
    @Override
    public void insert(T data) {
        //do nothing //取消insert操作
    }


    /**
     * 测试
     *
     * @param args
     */
    public static void main(String args[]) {

        String[] levelorderArray = {"A", "B", "C", "D", "E", "F"};
        CompleteBinaryTree<String> cbtree = new CompleteBinaryTree<>(levelorderArray);
        System.out.println("先根遍历:" + cbtree.preOrder());
        System.out.println("非递归先根遍历:" + cbtree.preOrderTraverse());
        System.out.println("中根遍历:" + cbtree.inOrder());
        System.out.println("非递归中根遍历:" + cbtree.inOrderTraverse());
        System.out.println("后根遍历:" + cbtree.postOrder());
        System.out.println("非递归后根遍历:" + cbtree.postOrderTraverse());
        System.out.println("查找最大结点(根据搜索二叉树):" + cbtree.findMax());
        System.out.println("查找最小结点(根据搜索二叉树):" + cbtree.findMin());
        System.out.println("判断二叉树中是否存在E:" + cbtree.contains("E"));
    }

}