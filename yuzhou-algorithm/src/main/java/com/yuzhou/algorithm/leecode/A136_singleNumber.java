package com.yuzhou.algorithm.leecode;

class A136_singleNumber {
    /**
     * 交换律：a ^ b ^ c <=> a ^ c ^ b
     * <p>
     * 任何数于0异或为任何数 0 ^ n = n
     * <p>
     * 相同的数异或为0: n ^ n = 0
     *
     * @param nums
     * @return
     */
    public static int singleNumber(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int ans = 0;
        for (int i = 0; i < nums.length; i++) {
            ans ^= nums[i];
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{2, 2, 1};
        System.out.println(singleNumber(nums));
    }
}