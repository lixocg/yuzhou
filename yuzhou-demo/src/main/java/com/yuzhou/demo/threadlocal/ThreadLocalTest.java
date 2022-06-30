package com.yuzhou.demo.threadlocal;

public class ThreadLocalTest {

    private static ThreadLocal<String> TL = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        TL.set("abc");

        new Thread(()->TL.set("bcd")).start();

        Thread.sleep(Integer.MAX_VALUE);
    }
}
