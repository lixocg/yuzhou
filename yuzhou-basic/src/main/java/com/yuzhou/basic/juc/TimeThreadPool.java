package com.yuzhou.basic.juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 扩展线程池，线程执行前后增加行为，线程池关闭后增加行为
 */
public class TimeThreadPool extends ThreadPoolExecutor {
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    private final AtomicLong numTasks = new AtomicLong();

    private final AtomicLong totalTime = new AtomicLong();

    public TimeThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);

        System.out.println(String.format("Thraed %s : start %s", t, r));
        startTime.set(System.nanoTime());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();

            numTasks.incrementAndGet();
            totalTime.addAndGet(taskTime);

            System.out.println(String.format("Thread %s : end %s, time = %dns", t, r, taskTime));
        } finally {
            super.afterExecute(r, t);
        }
    }

    @Override
    protected void terminated() {
        try {
            System.out.println(String.format("Treminated: avg time = %dns", totalTime.get() / numTasks.get()));
        } finally {
            super.terminated();
        }
    }


    public static void main(String[] args) {
        TimeThreadPool pool = new TimeThreadPool(10, 20, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));



        for (int i = 0; i < 20; i++) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(".....task....");
                }
            };
            pool.execute(task);
        }

        pool.shutdown();
    }
}
