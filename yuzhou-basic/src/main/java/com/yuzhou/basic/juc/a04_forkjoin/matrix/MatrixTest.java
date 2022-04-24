package com.yuzhou.basic.juc.a04_forkjoin.matrix;

/**
 * 矩阵乘法运算
 */
public class MatrixTest {
    public static void main(String[] args) {
        for(int i = 0;i<10000;i++){
            compute();
        }
    }

    public static void  compute(){
        int N = 2000;
        Matrix a = new Matrix(N, N);
        a.randomMatrix(10);
//        a.print();

        Matrix b = new Matrix(N, N);
        b.randomMatrix(10);
//        b.print();

        long s0 = System.currentTimeMillis();
        Matrix c = a.multiply(b);
//        c.print();
        double time1 = System.currentTimeMillis() - s0;
        System.out.println("串行计算耗时:" + time1 + " ms");



        long s1 = System.currentTimeMillis();
        Matrix c1 = a.parallelMultiply(b);
//        c1.print();
        double time2 = System.currentTimeMillis() - s1;
        System.out.println("并行计算耗时:" + time2 + " ms");

        System.out.println("串行/并行时间比:" + time1 / time2);
//        System.err.println("单颗CPU内核数 = " + Runtime.getRuntime().availableProcessors());
    }
}
