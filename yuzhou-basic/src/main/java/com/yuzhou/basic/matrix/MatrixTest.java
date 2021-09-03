package com.yuzhou.basic.matrix;

public class MatrixTest {
    public static void main(String[] args) {
        int N = 4;
        Matrix a = new Matrix(N, N);
        a.randomMatrix(3);
//        a.print();

        Matrix b = new Matrix(N, N);
        b.randomMatrix(3);
//        b.print();

        long s0 = System.nanoTime();
        Matrix c = a.multiply(b);
        c.print();
        double time1 = (System.nanoTime() - s0) / 1e9;
        System.out.println("串行计算耗时:" + time1);


        a.parallelMultiply(b);

        long s1 = System.nanoTime();
        Matrix c1 = a.parallelMultiply(b);
        c1.print();
        double time2 = (System.nanoTime() - s1) / 1e9;
        System.out.println("并行计算耗时:" + time2);

        System.out.println("串行/并行时间比:" + time1 / time2);
        System.err.println("单颗CPU内核数 = " + Runtime.getRuntime().availableProcessors());
    }
}
