package com.yuzhou.basic.juc;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorTest {


    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(2);

        ThreadFactory factory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(Thread.currentThread().getThreadGroup(), r,
                        "thread-" + threadNumber.getAndIncrement(),
                        0);
                if (t.isDaemon())
                    t.setDaemon(false);
                if (t.getPriority() != Thread.NORM_PRIORITY)
                    t.setPriority(Thread.NORM_PRIORITY);
                return t;
            }
        };

        RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("被拒绝...");
            }
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,
                2,
                2,
                TimeUnit.SECONDS,
                queue,
                factory,
                rejectedExecutionHandler);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("task run ...");
            }
        };

        for (int i = 0; i < 4; i++) {
            executor.submit(task);
        }

        for (int i = 0; i < 4; i++) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("corePoolSize大小:" + executor.getPoolSize());
        }

        for (int i = 0; i < 4; i++) {
            executor.submit(task);
        }


        for (int i = 0; i < 4; i++) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("重新执行后corePoolSize大小:" + executor.getPoolSize());
        }

        latch.await();
    }
}
