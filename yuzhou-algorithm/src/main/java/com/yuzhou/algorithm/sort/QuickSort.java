package com.yuzhou.algorithm.sort;

import java.util.Arrays;

public class QuickSort {

    public static void quickSort(int[] arr, int start, int end) {
        //递归退出条件
        if (start >= end) {
            return;
        }

        //设定起始元素为基准元素
        int pivot = arr[start];

        //low为由左向右移动游标
        int low = start;

        //high为由右向左移动游标
        int high = end;

        while (low < high) {
            //从右向左找不小于基准的元素
            while (arr[high] >= pivot && low < high) {
                high -= 1;
            }
            //找到high指向元素比pivot小，low指向替换为high指向元素
            arr[low] = arr[high];

            //从左向右找比基准大的元素
            while (arr[low] < pivot && low < high) {
                low += 1;
            }
            //找到low指向元素比pivot大，hight指向替换为low指向元素
            arr[high] = arr[low];
        }
        //一轮找完复位基准元素,左边元素全部小于基准元素，右边元素全部大于等于基准元素
        arr[low] = pivot;

        //递归对基准元素左右排序
        quickSort(arr, start, low - 1);
        quickSort(arr, low + 1, end);
    }

    public static void main(String[] args) {
        int[] arr = {54, 26, 93, 17, 77, 31, 44, 55, 20};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
