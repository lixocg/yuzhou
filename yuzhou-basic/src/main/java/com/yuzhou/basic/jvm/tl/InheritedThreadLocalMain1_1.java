package com.yuzhou.basic.jvm.tl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InheritedThreadLocalMain1_1 {

    private static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    /**模拟处理请求的线程池*/
    private static ExecutorService REQ_POOL = Executors.newFixedThreadPool(1);

    /**模拟真正处理任务的线程池*/
    private static ExecutorService TASK_POOL = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            final int a = i;
            REQ_POOL.submit(() -> {
                        Thread.currentThread().setName("请求线程" + a);
                        String parentName = Thread.currentThread().getName();
                        threadLocal.set(parentName);
                        System.out.println("请求线程的ThreadLocal：" + threadLocal.get());
                        TASK_POOL.submit(new Task(parentName));
                    }
            );
        }
    }

    public static class Task implements Runnable {

        private String pName;

        public Task(String pName){
            this.pName = pName;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(pName + " 的儿子线程");
            System.out.println(pName + "的儿子线程的ThreadLocal：" + threadLocal.get());
        }
    }
}