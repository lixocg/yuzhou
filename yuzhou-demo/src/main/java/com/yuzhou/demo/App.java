package com.yuzhou.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
public class App {

    private static AtomicInteger cnt = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello World!");

        String str = "h_j";
        List<String> strString = Arrays.asList(str.split("_"));
//        strString.add("fff");
        System.out.println(strString);

        long l = BigDecimal.valueOf(12).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.UP)
                .longValue();
        System.out.println(l);

        List<byte[]> arr = new ArrayList<>();
        while (true) {
            byte[] bytes = new byte[1024 * 1024];
            arr.add(bytes);
            Thread.sleep(100);
        }
    }
}
