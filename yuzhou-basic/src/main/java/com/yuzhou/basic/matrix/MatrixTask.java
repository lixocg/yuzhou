package com.yuzhou.basic.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 超快并行执行的多核心CPU矩阵乘法在数据统计方面的应用
 */
public class MatrixTask extends RecursiveAction {
    private final Matrix a, b, c;
    private int start;
    private int end;
    private final static int THRESHOLD = 100;

    public MatrixTask(Matrix a, Matrix b, Matrix c) {
        this(a, b, c, 0, a.getRows());
    }

    private MatrixTask(Matrix a, Matrix b, Matrix c, int start, int end) {
        if (a.getCols() != b.getRows()) {
            throw new IllegalArgumentException("rows/columns mismatch");
        }
        this.a = a;
        this.b = b;
        this.c = c;
        this.start = start;
        this.end = end;
    }

    @Override
    public void compute() {
        if (end - start <= THRESHOLD) {
            // 如果任务足够小,直接计算:
            for (int i = start; i < end; i++) {
                multiplyRowByColumn(a, b, c, start, end);
                return;
            }
        }
        // 任务太大,一分为二:
        int middle = (end + start) / 2;
//        System.out.println(String.format("split %d~%d ==> %d~%d, %d~%d", start, end, start, middle, middle, end));
        invokeAll(new MatrixTask(a, b, c, start, middle), new MatrixTask(a, b, c, middle, end));

    }

    public static void multiplyRowByColumn(Matrix a, Matrix b, Matrix c, int start, int end) {
//        System.out.println("thread---"+Thread.currentThread());
        for (int i = start; i < end; i++) {
            for (int j = 0; j < b.getCols(); j++) {
                for (int k = 0; k < a.getCols(); k++) {
                    c.setCellValue(i, j, c.getCellValue(i, j) + a.getCellValue(i, k) * b.getCellValue(k, j));
                }
            }
        }
    }

    public static void print(Matrix mat) {
        for (int i = 0; i < mat.getRows(); i++) {
            for (int j = 0; j < mat.getCols(); j++) {
                System.out.print(mat.getCellValue(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        long start = System.nanoTime(); /* A矩阵：4行3列 5 2 4 3 8 2 6 0 4 0 1 6 */
        Matrix a = new Matrix(4, 3);
        a.randomMatrix(10);
        a.print();

        Matrix b = new Matrix(3, 2);
        b.randomMatrix(10);
        b.print();

        Matrix c = new Matrix(4, 2);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new MatrixTask(a, b, c));
        c.print();
        long end = System.nanoTime();
        double timeTaken = (end - start) / 1e9;
        System.out.println("Recursive Matrix Multiply Time Taken in seconds：" + timeTaken);
        System.err.println("单颗CPU内核数 = " + Runtime.getRuntime().availableProcessors());
    }
}