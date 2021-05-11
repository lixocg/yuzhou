package com.yuzhou.algorithm.leecode;

/**
 * https://www.zhihu.com/question/23995189/answer/1160796300?utm_source=qq&utm_medium=social&utm_oi=674171077241081856
 * <p>
 * <p/>
 * 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 * <p>
 * 示例:
 * <p>
 * 输入: [-2,1,-3,4,-1,2,1,-5,4],
 * 输出: 6
 * 解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximum-subarray
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class A53_maxSubArray {

    /***
     * 动态规划：
     * 1.将问题拆解成多阶段子问题
     * 2.分离指标函数
     * 3.确定状态转移方程
     * <p/>
     * 1.拆解：以第n个元素结尾的最大子序列和:s(n)
     * 2.当加入一个元素E[n+1]后，如果s(n)小于0，忽略，否则求和
     * 3.状态转移方程：s(n+1) = max(s(n),0) + E[n+1]
     * @param nums
     * @return
     */
    public static int maxSubArray(int[] nums) {
        //1.构造子问题：dp[n] : 代表以第n个元素结尾的最大子序列和
        int[] dp = new int[nums.length];
        //2.初始化状态
        dp[0] = nums[0];
        int ans = nums[0];
        for (int i = 1; i < nums.length; i++) {
            //3.分离指标函数，确定状态转移方程
            dp[i] = Math.max(dp[i - 1], 0) + nums[i];


            ans = Math.max(dp[i], ans);
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] nums = {-2, 1};
        System.out.println(maxSubArray(nums));
    }
}
