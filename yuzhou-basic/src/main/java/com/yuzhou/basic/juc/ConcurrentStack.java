package com.yuzhou.basic.juc;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 非阻塞栈，Java并发编程实战P271
 */
public class ConcurrentStack<E> {
    private final AtomicReference<Node<E>> top = new AtomicReference<>();

    public void push(E item) {
        Node<E> newNode = new Node<>(item);
        Node<E> oldNode;

        do {
            oldNode = top.get();
            newNode.next = oldNode;
        } while (top.compareAndSet(oldNode, newNode));
    }

    public E pop() {
        Node<E> oldNode;
        Node<E> newNode;

        do {
            oldNode = top.get();
            if (oldNode == null) {
                return null;
            }
            newNode = oldNode.next;
        } while (top.compareAndSet(oldNode, newNode));
        return oldNode.item;
    }

    private static class Node<E> {
        public E item;
        public Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }
}
