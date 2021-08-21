package com.yuzhou.datastruct.linkedlist.doublelinkedlist;

import com.yuzhou.datastruct.Node;

/**
 * 升序排序链表，继承LoopHeadDILinkedList
 */
public class SortLoopHeadDIlinkedList<T extends Comparable<? extends T>> extends LoopHeadDILinkedList<T> {
    /**
     * 顺序插入
     *
     * @param data
     * @return
     */
    @Override
    public boolean add(T data) {

        if (data == null || !(data instanceof Comparable))
            throw new NullPointerException("data can\'t be null or data instanceof Comparable must be true");

        Comparable cmp = data;//这里需要转一下类型,否则idea编辑器上检验不通过.

        //如果data值比最后一个结点大,那么直接调用父类方法,在尾部添加.
        if (this.isEmpty() || cmp.compareTo(this.head.prev.data) > 0) {
            return super.add(data);
        }

        Node<T> p = this.head.next;
        //查找插入点
        while (p != head && cmp.compareTo(p.data) > 0)
            p = p.next;

        Node<T> q = new Node<>(data, p.prev, p);
        p.prev.next = q;
        p.prev = q;

        return true;
    }

    public static void main(String[] args) {
        SortLoopHeadDIlinkedList<Integer> list = new SortLoopHeadDIlinkedList<>();
        list.add(50);
        list.add(40);
        list.add(80);
        list.add(20);
        System.out.println("init list-->" + list.toString());
    }
}