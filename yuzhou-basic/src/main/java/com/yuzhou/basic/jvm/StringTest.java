package com.yuzhou.basic.jvm;

public class StringTest {
    public static void main(String[] args) {
        String s1 = new String("1");
        s1 = s1.intern();

        String s2 = "1";
        System.out.println(s1 == s2);
        System.out.println(s1.hashCode());
        System.out.println(s2.hashCode());
    }
}
