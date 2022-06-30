package com.yuzhou.basic.jvm.tl;

public class ThreadLocalMain {

    public static final ThreadLocal<String> CONTEXT = new InheritableThreadLocal<>();

    public static void main(String[] args) throws Exception {
        // 主线程设置值
        CONTEXT.set("mainVal");
        System.out.println(Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
        Thread t = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
            // 设置当前线程中的值
            CONTEXT.set("subVal");
            System.out.println("重新设置之后，" + Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
            System.out.println(Thread.currentThread().getName() + "线程执行结束");
        });
        t.start();
        // 等待子线程运行结束
        t.join();
        System.out.println(Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
    }
}