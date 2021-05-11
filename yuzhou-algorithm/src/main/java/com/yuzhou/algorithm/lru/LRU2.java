package com.yuzhou.algorithm.lru;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LRU2 {
    private final static int DEFAULT_CAPACITY = 10;

    private int capacity;

    private LinkedHashMap<String, String> cache;

    public LRU2(final int capacity) {
        this.capacity = capacity;
        cache = new LinkedHashMap<String, String>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return cache.size() > capacity;
            }
        };
    }

    public LRU2() {
        this(DEFAULT_CAPACITY);
    }

    public void put(String key, String val) {
        cache.put(key, val);
    }

    public String get(String key) {
        return cache.get(key);
    }

    public void print() {
        Set<Map.Entry<String, String>> entries = cache.entrySet();
        entries.forEach((entry) -> {
            System.out.print(entry.getValue() + ",");
        });
        System.out.println();
    }

    public static void main(String[] args) {
        LRU2 lru = new LRU2(3);
        lru.put("a", "a1");
        lru.print();
        lru.put("b", "b1");
        lru.get("a");
        lru.print();
        lru.put("c", "c1");
        lru.put("d", "d1");
        lru.print();
    }
}
