package com.yuzhou.demo.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws Exception {
        String val = "haha";
        System.out.println("thread:"+Thread.currentThread().getName()+"设置："+val);
        Namespaces.set(val);
        executorService.submit(new Task());

        val = "haha1";
        System.out.println("thread:"+Thread.currentThread().getName()+"设置："+val);
        Namespaces.set(val);
        executorService.submit(new Task());

    }

    static class Task implements Runnable{

        @Override
        public void run() {
            System.out.println("thread:"+Thread.currentThread().getName()+"获取val:" + Namespaces.get());
        }
    }
}
