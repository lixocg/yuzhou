package com.yuzhou.basic.jvm;

public class User {
    private static int x = 0;

    static {
        x = 10;
        System.out.println("static init ....x = " + x);
    }
}
