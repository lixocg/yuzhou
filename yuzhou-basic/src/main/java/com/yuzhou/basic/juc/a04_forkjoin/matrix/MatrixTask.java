package com.yuzhou.basic.juc.a04_forkjoin.matrix;

import java.util.concurrent.RecursiveAction;

/**
 * 矩阵并行执行任务
 */
public class MatrixTask extends RecursiveAction {
    private final Matrix a, b, c;
    private int start;
    private int end;
    private final static int THRESHOLD = 50;

    public MatrixTask(Matrix a, Matrix b, Matrix c) {
        this(a, b, c, 0, a.getRows());
    }

    private MatrixTask(Matrix a, Matrix b, Matrix c, int start, int end) {
        if (a.getCols() != b.getRows()) {
            throw new IllegalArgumentException("行列不匹配");
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
                multiplyByRows(a, b, c, start, end);
                return;
            }
        }
        // 任务太大,一分为二:
        int middle = (end + start) / 2;
//        System.out.println(String.format("split %d~%d ==> %d~%d, %d~%d", start, end, start, middle, middle, end));
        invokeAll(new MatrixTask(a, b, c, start, middle), new MatrixTask(a, b, c, middle, end));

    }

    public static void multiplyByRows(Matrix a, Matrix b, Matrix c, int start, int end) {
        for (int i = start; i < end; i++) {
            for (int j = 0; j < b.getCols(); j++) {
                for (int k = 0; k < a.getCols(); k++) {
                    c.setCellValue(i, j, c.getCellValue(i, j) + a.getCellValue(i, k) * b.getCellValue(k, j));
                }
            }
        }
    }
}