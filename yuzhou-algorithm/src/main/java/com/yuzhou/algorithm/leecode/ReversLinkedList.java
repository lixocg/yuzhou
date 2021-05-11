package com.yuzhou.algorithm.leecode;

public class ReversLinkedList {

    public static Node reverse(Node head){
        Node pre = null;
        Node cur = head;
        while (cur != null){
            Node tempNext = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tempNext;
        }

        return pre;
    }

    private static class Node{
        private int val;
        private Node next;


    }

    public static void main(String[] args) {
        Node node = new Node();
    }
}
