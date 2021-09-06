package com.yuzhou.basic.juc.a04_forkjoin.sum;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

/**
 * 数值相加
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // 创建2000个随机数组成的数组:
        long[] array = new long[200000];
        long expectedSum = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = random();
        }

        long s1 = System.currentTimeMillis();
        for (int i = 0; i < array.length; i++) {
            expectedSum += array[i];
        }
        System.out.println("Expected sum: " + expectedSum + ",耗时:" + (System.currentTimeMillis() - s1) + " ms");

        long s2 = System.currentTimeMillis();
        long sum = LongStream.of(array).parallel().sum();
        System.out.println("stream sum: " + sum + ",耗时:" + (System.currentTimeMillis() - s2) + " ms");


        // fork/join:
        ForkJoinTask<Long> task = new SumTask(array, 0, array.length);
        long startTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool(20);
        Long result = forkJoinPool.invoke(task);
//        Long result = ForkJoinPool.commonPool().invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("Fork/join sum: " + result + " in " + (endTime - startTime) + " ms.");


    }

    static Random random = new Random(0);

    static long random() {
        return random.nextInt(10000);
    }
}
