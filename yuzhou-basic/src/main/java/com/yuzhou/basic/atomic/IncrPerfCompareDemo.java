package com.yuzhou.basic.atomic;

import java.util.concurrent.atomic.AtomicLong;

public class IncrPerfCompareDemo {

    public static void main(String[] args) {
        SyncIncr syncIncr = new SyncIncr();

        int N = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i < N; i++) {
            new Thread(syncIncr::incr, "thread-" + (i + 1)).start();
        }

    }
}

class SyncIncr {
    private long val;

    public synchronized long incr() {
        return ++val;
    }
}

class CasIncr {
    private final AtomicLong atomicLong = new AtomicLong(0);

    public long incr() {
        return atomicLong.incrementAndGet();
    }
}
