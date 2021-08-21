package com.yuzhou.datastruct;

/**
 * 链表节点
 */
public class Node<T> {
    //数据域
    public T data;

    //地址域
    public Node<T> next;
    public Node<T> prev;

    public Node(){}

    public Node(T data) {
        this.data = data;
    }

    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    public Node(T data, Node<T> prev, Node<T> next) {
        this.data = data;
        this.prev = prev;
        this.next = next;
    }

    public String toString() {
        return this.data.toString();
    }
}