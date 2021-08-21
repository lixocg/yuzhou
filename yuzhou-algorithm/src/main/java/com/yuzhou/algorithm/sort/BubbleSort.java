package com.yuzhou.algorithm.sort;

public class BubbleSort {

    public static void main(String[] args) {
        int[] arr = {4,5,3,5,2,4,1};
        sort(arr);

        for(int e : arr){
            System.out.println(e);
        }
    }

    public static void sort(int[] arr) {
        for (int j = arr.length; j > 0; j--) {
            for (int i = 1; i < j; i++) {
                if(arr[i-1] > arr[i]) {
                    swap(arr, i - 1, i);
                }
            }
        }
    }

    private static void swap(int[] arr, int xIndex, int yIndex) {
        int temp = arr[xIndex];
        arr[xIndex] = arr[yIndex];
        arr[yIndex] = temp;
    }
}
