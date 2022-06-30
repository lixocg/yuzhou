package com.yuzhou.basic.jvm.tl;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.junit.internal.runners.statements.RunAfters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InheritedThreadLocalMain1 {

    /**
     * ThreadLocal变量，每个线程都有一个副本，互不干扰
     */
    public static final ThreadLocal<String> CONTEXT = new InheritableThreadLocal<>();

    public static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws Exception {
        // 主线程设置值
        CONTEXT.set("主线程val");
        System.out.println(Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());

        for (int i = 0; i < 10; i++) {
            executorService.submit(new Task());
        }
        // 等待所有线程执行结束
        Thread.sleep(1000L);
        System.out.println(Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
            // 设置当前线程中的值
            System.out.println(Thread.currentThread().getName() + "线程执行结束");
        }
    }

}