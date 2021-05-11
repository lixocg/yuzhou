package com.yuzhou.algorithm.leecode;

class A21_mergeTwoLists {
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode ans = new ListNode(-1);

        ListNode curNode = ans;
        while (l1 != null && l2 != null) {
            int l1val = l1.val;
            int l2val = l2.val;
            if (l1val < l2val) {
                curNode.next = l1;
                curNode = curNode.next;
                l1 = l1.next;
            } else {
                curNode.next = l2;
                curNode = curNode.next;
                l2 = l2.next;
            }
        }
        if(l1 == null){
            curNode.next = l2;
        }else {
            curNode.next = l1;
        }

        return ans.next;
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        l1.next = new ListNode(2);
        l1.next.next = new ListNode(3);

        ListNode l2 = new ListNode(2);
        l2.next = new ListNode(3);
        l2.next.next = new ListNode(6);

        ListNode listNode = mergeTwoLists(l1, l2);
        while (listNode != null) {
            System.out.println(listNode.val);
            listNode = listNode.next;
        }
    }
}