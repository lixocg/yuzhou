package com.yuzhou.datastruct.linkedlist;


import com.yuzhou.datastruct.Node;

public class LoopLinkedList {
    public static void main(String[] args) {

    }

    public static boolean isLoop(Node node){
        if(node == null || node.next == null){
            return false;
        }
        Node slow = node;
        Node fast = node.next;
        while (fast.next != null){
            if(fast.data == slow.data){
                return true;
            }

            fast = fast.next;
        }
        return false;
    }
}
