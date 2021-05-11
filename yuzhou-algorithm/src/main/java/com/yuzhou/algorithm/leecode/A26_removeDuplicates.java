package com.yuzhou.algorithm.leecode;

class A26_removeDuplicates {
    public static int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int ans = nums.length;

        int unique = nums[0];
        int offset = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == unique) {
                ans--;
                offset++;
            } else {
                System.out.println(i + "---" + (offset));
                nums[i - offset] = nums[i];
            }
            unique = nums[i];
        }
        //System.out.println(Arrays.toString(nums));
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(removeDuplicates(new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4}));
    }
}