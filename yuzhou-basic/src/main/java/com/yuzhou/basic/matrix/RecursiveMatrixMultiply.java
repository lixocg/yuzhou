package com.yuzhou.basic.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RecursiveMatrixMultiply extends RecursiveAction {
    private final Matrix a, b, c;
    private final int row;

    public RecursiveMatrixMultiply(Matrix a, Matrix b, Matrix c) {
        this(a, b, c, -1);
    }

    public RecursiveMatrixMultiply(Matrix a, Matrix b, Matrix c, int row) {
        if (a.getCols() != b.getRows()) {
            throw new IllegalArgumentException("rows/columns mismatch");
        }
        this.a = a;
        this.b = b;
        this.c = c;
        this.row = row;
    }

    @Override
    public void compute() {
        if (row == -1) {
            List tasks = new ArrayList<>();
            for (int row = 0; row < a.getRows(); row++) {
                tasks.add(new RecursiveMatrixMultiply(a, b, c, row));
            }
            invokeAll(tasks);
        } else {
            multiplyRowByColumn(a, b, c, row);
        }
    }

    public static void multiplyRowByColumn(Matrix a, Matrix b, Matrix c, int row) {
        for (int j = 0; j < b.getCols(); j++) {
            for (int k = 0; k < a.getCols(); k++) {
                c.setCellValue(row, j, c.getCellValue(row, j) + a.getCellValue(row, k) * b.getCellValue(k, j));
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
       a.randomMatrix(5);
        print(a); /* B矩阵：3行2列 2 4 1 3 3 2 */
        Matrix b = new Matrix(3, 2);
        b.randomMatrix(5);
        print(b); /* C矩阵：4行2列  C = A * B */
        Matrix c = new Matrix(4, 2);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new RecursiveMatrixMultiply(a, b, c));
        print(c);
        long end = System.nanoTime();
        double timeTaken = (end - start) / 1e9;
        System.out.println("Recursive Matrix Multiply Time Taken in seconds：" + timeTaken);
        System.err.println("单颗CPU内核数 = " + Runtime.getRuntime().availableProcessors());
    }
}