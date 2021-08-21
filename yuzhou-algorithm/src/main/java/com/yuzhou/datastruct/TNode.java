package com.yuzhou.datastruct;


/**
 * 树节点
 * @param <T>
 */
public class TNode<T extends Comparable> {
    public TNode<T> left;//左结点

    public TNode<T> right;//右结点

    public T data;

    public int height;//当前结点的高度

    public TNode() {
    }

    public TNode(T data) {
        this(null, null, data);
    }

    public TNode(TNode<T> left, TNode<T> right, T data) {
        this(left, right, data, 0);
    }

    public TNode(TNode<T> left, TNode<T> right, T data, int height) {
        this.left = left;
        this.right = right;
        this.data = data;
        this.height = height;
    }

    /**
     * 判断是否为叶子结点
     *
     * @return
     */
    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }
}
