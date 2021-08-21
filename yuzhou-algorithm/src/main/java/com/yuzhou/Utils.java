package com.yuzhou;

import java.util.Random;

public class Utils {
    public static void print(int[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            if (i == arr.length - 1) {
                sb.append(arr[i]);
            } else {
                sb.append(arr[i] + ",");
            }
            if (i == 9 && i != (arr.length - 1)) {
                sb.append(arr[i]);
                break;
            }
        }
        System.out.println(sb.toString());
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static int[] random(int num) {
        Random random = new Random();
        int[] arr = new int[num];
        for (int j = 0; j < num; j++) {
            int val = random.nextInt(num * 5);
            arr[j] = val;
        }
        return arr;
    }
}
