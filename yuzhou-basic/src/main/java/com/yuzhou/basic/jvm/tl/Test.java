package com.yuzhou.basic.jvm.tl;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class Test {
    private static final String THREAD_LOCAL_VAR= "threadLocals";

    private static ThreadLocal<String> CTX = new ThreadLocal<>();

    public static void main(String[] args) {
        new Thread(new Task()).start();
        new Thread(new Task()).start();
    }

    private static class Task implements Runnable{

        @Override
        public void run() {
            try {
                CTX.set("aaa");
                Class<? extends Thread> clazz = Thread.currentThread().getClass();
                Field declaredField = clazz.getDeclaredField(THREAD_LOCAL_VAR);
                declaredField.setAccessible(true);
                Object o = declaredField.get(Thread.currentThread());
                System.out.println(o);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
