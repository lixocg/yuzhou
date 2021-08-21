package com.yuzhou.algorithm.sort;

import com.yuzhou.Utils;

public class SelectionSort{
    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            //假设tmptIndex下标对应值最小
            int temptIndex = i;

            //找到真正最小值的下标-tmptIndex
            for (int j = i + 1; j < arr.length; j++) {
                if(arr[temptIndex] > arr[j]){
                    temptIndex = j;
                }
            }
            //此时tmptIndex下标对应的值最小
            //交换假设最小值和真是最小值
            Utils.swap(arr,i,temptIndex);
        }
    }

    public static void main(String[] args) {
        int[] arr = Utils.random(20);
        selectionSort(arr);
        Utils.print(arr);
    }
}
