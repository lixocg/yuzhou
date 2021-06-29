package com.yuzhou.basic.jvm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * -Xms20M -Xmx20M -XX:+PrintGC
 */
public class T01_FullGCProblem {
    private static class CardInfo {
        BigDecimal price = new BigDecimal(0.0);
        String name = "张三";
        int age = 5;
        Date birthday = new Date();

        private void m() {
        }
    }

    private static ScheduledThreadPoolExecutor executor =
            new ScheduledThreadPoolExecutor(50, new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws Exception {
        executor.setMaximumPoolSize(50);

        for (; ; ) {
            modelFit();

            Thread.sleep(500);
        }
    }

    private static void modelFit() {
        List<CardInfo> taskList = getALlCardInfo();

        taskList.forEach(info -> {
            executor.scheduleWithFixedDelay(() -> {
                info.m();
            }, 2, 3, TimeUnit.SECONDS);
        });

    }

    private static List<CardInfo> getALlCardInfo() {
        List<CardInfo> taskList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            CardInfo cardInfo = new CardInfo();
            taskList.add(cardInfo);
        }
        return taskList;
    }
}
