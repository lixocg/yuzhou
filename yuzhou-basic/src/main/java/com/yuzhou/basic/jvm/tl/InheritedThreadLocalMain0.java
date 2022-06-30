package com.yuzhou.basic.jvm.tl;

public class InheritedThreadLocalMain0 {

    /**
     * ThreadLocal变量，每个线程都有一个副本，互不干扰
     */
    public static final ThreadLocal<String> CONTEXT = new InheritableThreadLocal<>();

    public static void main(String[] args) throws Exception {
        // 主线程设置值
        CONTEXT.set("主线程val");
        System.out.println(Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
            // 设置当前线程中的值
            CONTEXT.set("子线程val");
            System.out.println("重新设置之后，" + Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
            System.out.println(Thread.currentThread().getName() + "线程执行结束");
        }).start();
        // 等待所有线程执行结束
        Thread.sleep(1000L);
        System.out.println(Thread.currentThread().getName() + "线程ThreadLocal中的值：" + CONTEXT.get());
    }

}