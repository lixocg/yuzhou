package com.yuzhou.basic.jvm.tl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalMem {

    private static final ThreadLocal<byte[]> CONTEXT = new ThreadLocal<>();

    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(20 * 1000);

        print("开启线程.....");

        executorService.submit(new Task());
//        Thread t = new Thread(new Task());
//        t.start();
//        t.join();

        Thread.sleep(10 * 1000);
        System.gc();
        print("手动GC ...");
        Thread.currentThread().join();
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            try {
                byte[] data = new byte[50 * 1024 * 1024];
                CONTEXT.set(data);
                print("设置完成...");
                CONTEXT.remove();
                print("remove完成...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void print(String msg) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " " + " " + msg);
    }

}
