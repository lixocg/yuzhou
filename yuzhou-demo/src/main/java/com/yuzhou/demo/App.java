package com.yuzhou.demo;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
public class App {

    private static AtomicInteger cnt = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        String s = "101:longTimeExportResult:9ad3b9ddf21e6096ee460cbd960fc4af@销售统计导出@20220825094747";
        String[] split = s.split("@");
        Arrays.stream(split).forEach(System.out::println);

        testStrictfp();
        testStrictfp1();
    }

    public strictfp static void testStrictfp() {
        float aFloat = 0.6666666666666666666f;
        double aDouble = 0.88888888888888888d;
        double sum = aFloat + aDouble;
        System.out.println("strict sum: " + sum);
    }

    public static void testStrictfp1() {
        float aFloat = 0.6666666666666666666f;
        double aDouble = 0.88888888888888888d;
        double sum = aFloat + aDouble;
        System.out.println("sum: " + sum);
    }
}
