package com.yuzhou.algorithm.lru;

public class LRU {
    private final static int DEFAULT_CAPACITY = 10;

    private final int capacity;
    private Node head;
    private Node tail;
    private int size;

    public LRU(int capacity) {
        this.capacity = capacity;
    }

    public LRU() {
        this(DEFAULT_CAPACITY);
    }

    //向LRU缓存加入元素,添加元素到头部
    public void put(String key, String val) {
        Node node = new Node(key, val);

        //缓存满了，需要移除链表最后的元素
        if (size == capacity) {
            removeLast();
        }

        //添加元素到链表头部
        Node p = head;
        node.next = p;
        node.prev = null;
        head = node;
        //头元素为空的情况
        if (p == null) {
            tail = node;
        } else {
            p.prev = node;
        }
        size++;
    }

    private void removeLast() {
        if (size == 0) {
            return;
        }
        tail = tail.prev;
        tail.next = null;
        size--;
    }

    //向LRU缓存获取元素
    //存在-在头部返回，不在头部移动到头部
    public String get(String key) {
        int index = indexOf(key);
        if (index == -1) {
            return null;
        }
        if (index != 0) {
            //不在头部移动到头部
            moveToHead(index);
        }

        return head.val;
    }

    private void moveToHead(int index) {
        Node p;
        //特殊处理尾结点
        if (index == size - 1) {
            p = tail;
            tail = tail.prev;
            tail.next = null;
        } else {
            p = getNodeByIndex(index);
            p.prev.next = p.next;
            p.next.prev = p.prev;
        }
        p.prev = null;
        p.next = head;
        head.prev = p;
        head = p;
    }

    private Node getNodeByIndex(int index) {
        Node p = head;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p;
    }

    private int indexOf(String key) {
        if (size == 0) {
            return -1;
        }
        int index = 0;
        Node p = head;
        while (p != null) {
            if (p.key.equals(key)) {
                return index;
            }
            index++;
            p = p.next;
        }
        return index;
    }


    private static class Node {
        private Node prev;
        private Node next;

        private String key;
        private String val;

        public Node(String key, String val) {
            this.key = key;
            this.val = val;
        }
    }

    private void print(){
        Node p = head;
        while (p != null){
            System.out.print(p.val+",");
            p = p.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        LRU lru = new LRU(3);
        lru.put("a","a");
        lru.print();

        lru.put("b","b");
        lru.print();

        lru.put("c","c");
        lru.print();

        lru.get("a");
        lru.print();

        lru.put("d","d");
        lru.print();

    }
}
