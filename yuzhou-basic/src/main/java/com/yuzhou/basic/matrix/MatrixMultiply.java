package com.yuzhou.basic.matrix;

/**
 * 单线程矩阵乘法
 */
public class MatrixMultiply {
    public static void main(String[] args) { /* A矩阵：4行3列 5 2 4 3 8 2 6 0 4 0 1 6 */
        long start = System.nanoTime();
        Matrix a = new Matrix(4, 3);
        a.randomMatrix(3);
        a.print();
        Matrix b = new Matrix(3, 2);
        b.randomMatrix(3);
        b.print();
        multiply(a, b).print();
        long end = System.nanoTime();
        double timeTaken = (end - start) / 1e9;
        System.out.println("Matrix Multiply Time Taken in seconds：" + timeTaken);
    }


    public static Matrix multiply(Matrix a, Matrix b) {
        if (a.getCols() != b.getRows()) {
            throw new IllegalArgumentException("rows/columns mismatch");
        }
        Matrix result = new Matrix(a.getRows(), b.getCols());
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < b.getCols(); j++) {
                for (int k = 0; k < a.getCols(); k++) {
                    result.setCellValue(i, j, result.getCellValue(i, j) + a.getCellValue(i, k) * b.getCellValue(k, j));
                }
            }
        }
        return result;
    }
}