package com.yuzhou.basic.jvm.tl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InheritedThreadLocalMain2 {

    /**
     * ThreadLocal变量，每个线程都有一个副本，互不干扰
     */
    public static final ThreadLocal<String> CONTEXT = new TransmittableThreadLocal<>();

    public static final ExecutorService executorService =
            TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(1));


    public static void main(String[] args) throws Exception {
        // 主线程设置值
        CONTEXT.set("主线程val");
        System.out.println(Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());

        Task t1 = new Task();
        Task t2 = new Task();
        executorService.submit(TtlRunnable.get(t1));
        executorService.submit(TtlRunnable.get(t1));
        // 等待所有线程执行结束
        Thread.sleep(1000L);
        System.out.println(Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
    }

    static class Task implements Runnable{

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
            // 设置当前线程中的值
            CONTEXT.set("子线程val");
            System.out.println("重新设置之后，" + Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
            System.out.println(Thread.currentThread().getName() + "线程执行结束");
        }
    }

}