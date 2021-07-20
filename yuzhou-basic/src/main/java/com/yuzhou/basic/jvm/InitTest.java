package com.yuzhou.basic.jvm;

public class InitTest {

    public static void main(String[] args) throws Exception {
        ClassLoader loader1 = ClassLoader.getSystemClassLoader();
        ClassLoader loader2 = ClassLoader.getSystemClassLoader();

        Class<?> clazz1 = loader1.loadClass("com.yuzhou.basic.jvm.User");
        Object o = clazz1.newInstance();

        Class<?> clazz2 = loader2.loadClass("com.yuzhou.basic.jvm.User");
        Object o1 = clazz2.newInstance();
    }

}
