package com.yuzhou;

public class Reverse {

    public static class Node {
        int val;
        Node next;

        public Node(int val){
            this.val = val;
        }
    }

    public static Node rmNTh(Node root, int n) {
        if (root == null) {
            return null;
        }

        //找到总长度
        int size = 0;
        Node p = root;
        while (p != null) {
            size++;
            p = p.next;
        }

        if (n > size) {
            return null;
        }

        int rmNth = size - n;

        Node q = root;
        Node prve = q;
        int index = 0;
        while (q != null) {
            if (index == rmNth) {
                prve.next = q.next;
            }else {
                prve = q;
            }
            q = q.next;
            index++;
        }
        return q;
    }

    public static void main(String[] args) {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;


        Node node = rmNTh(n1, 2);
        while(node != null){
            System.out.print(node.val);
            node = node.next;
        }
    }
}
