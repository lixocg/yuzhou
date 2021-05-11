package com.yuzhou.algorithm.leecode;

/**
 * Created by lixiongcheng on 2020/6/1.
 */
public class A206_reverseLinkedList {
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode n1 = new ListNode(2);
        ListNode n2 = new ListNode(3);
        ListNode n3 = new ListNode(4);

        head.next = n1;
        n1.next = n2;
        n2.next = n3;


        print(head);

        print(reverseList(head));
    }

    public static void print(ListNode head){
        String str = "";

        ListNode cur = head;
        while (cur.next != null){
            str += (cur.val + "->");

            cur = cur.next;
        }
        str += cur.val + "->NULL";
        System.out.println(str);
    }

    public static ListNode reverseList(ListNode head) {
        ListNode cur = head;
        ListNode pre = null;
        ListNode next = null;

        if (cur == null) {
            return null;
        }

        while (cur != null) {
            next = cur.next;
            cur.next = pre;

            pre = cur;
            cur = next;
        }

        return pre;

    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
