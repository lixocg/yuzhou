package com.yuzhou.basic.matrix;

import java.util.Random;

/**
 * 矩阵数据结构定义
 */
public class Matrix {
    private final int[][] matrix;

    private static Random random = new Random(1);

    public Matrix(int rows, int cols) {
        matrix = new int[rows][cols];
    }

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getCols() {
        return matrix[0].length;
    }

    public int getRows() {
        return matrix.length;
    }

    public int getCellValue(int row, int col) {
        return matrix[row][col];
    }

    public void setCellValue(int row, int col, int value) {
        matrix[row][col] = value;
    }

    /**
     * 串行计算矩阵乘法
     */
    public Matrix multiply(Matrix b) {
        System.out.println("thread======"+Thread.currentThread());
        if (this.getCols() != b.getRows()) {
            throw new IllegalArgumentException("rows/columns mismatch");
        }
        Matrix result = new Matrix(this.getRows(), b.getCols());
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < b.getCols(); j++) {
                for (int k = 0; k < this.getCols(); k++) {
                    result.setCellValue(i, j, result.getCellValue(i, j) + this.getCellValue(i, k) * b.getCellValue(k, j));
                }
            }
        }
        return result;
    }

    /**
     * 并行计算矩阵乘法
     */
    public Matrix parallelMultiply(Matrix b) {
        Matrix result = new Matrix(this.getRows(), b.getCols());
        PoolUtil.pool.invoke(new MatrixTask(this, b, result));
        return result;
    }

    /**
     * 生成随机数矩阵
     *
     * @param bound 数值边界
     */
    public void randomMatrix(int bound) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                setCellValue(row, col, random.nextInt(bound));
            }
        }
    }

    public void print() {
        String separator = " ";
        for (int row = 0; row < matrix.length; row++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int col = 0; col < matrix[row].length; col++) {
                stringBuilder.append(matrix[row][col]).append(separator);
            }
            System.out.println(stringBuilder.toString());
        }
        System.out.println();
    }
}