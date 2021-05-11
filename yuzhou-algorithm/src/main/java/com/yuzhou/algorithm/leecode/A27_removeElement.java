package com.yuzhou.algorithm.leecode;

import java.util.Arrays;

class A27_removeElement {
    //nums = [0,1,2,2,3,0,4,2], val = 2,
    public static int removeElement(int[] nums, int val) {
        int res = 0;
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] != val) {
                nums[res++] = nums[i];
            }
        }
        System.out.println(Arrays.toString(nums));
        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{0, 1, 2, 2, 3, 0, 4, 2};
        int val = 2;
        System.out.println(removeElement(nums, val));
    }
}