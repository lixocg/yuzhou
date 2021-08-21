package com.yuzhou.datastruct.linkedlist.xorlinkedlist;

/**
 * 异或结点
 */
public class XORNode<T> {
    public T data;
    public XORNode<T> ptrdiff;//异或指针

    public XORNode() {
    }

    public XORNode(T data, XORNode<T> ptrdiff) {
        this.data = data;
        this.ptrdiff = ptrdiff;
    }
}