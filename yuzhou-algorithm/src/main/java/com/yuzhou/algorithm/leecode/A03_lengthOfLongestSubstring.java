package com.yuzhou.algorithm.leecode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
 * <p>
 * 示例 1:
 * <p>
 * 输入: "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 示例 2:
 * <p>
 * 输入: "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * 示例 3:
 * <p>
 * 输入: "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 *      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-substring-without-repeating-characters
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class A03_lengthOfLongestSubstring {
    //abcabcbb

    /**
     * 思路：借助set集合+快慢指针，每次移动慢指针需要一个个移动，可以使用hash表来快速移动慢指针
     */
    public static int lengthOfLongestSubstring(String s) {
        if (s == null) {
            return 0;
        }

        Set<Character> set = new HashSet<>();
        int max = 0;

        //i-慢指针 j-快指针
        for (int i = 0, j = 0; j < s.length(); j++) {
            while (set.contains(s.charAt(j))) {
                set.remove(s.charAt(i));
                i++;

            }

            set.add(s.charAt(j));
            max = Math.max(max, set.size());
        }
        return max;
    }


    /***
     * 使用hash表来记录慢指针位置，提高移动效率
     */
    public static int lengthOfLongestSubstring2(String s) {
        if (s == null) {
            return 0;
        }

        Map<Character, Integer> map = new HashMap<>();
        int max = 0;

        //i-慢指针 j-快指针
        for (int i = 0, j = 0; j < s.length(); j++) {
            if (map.containsKey(s.charAt(j))) {
                i = Math.max(i, map.get(s.charAt(j))+1);
            }

            map.put(s.charAt(j), j);
            max = Math.max(max, j - i + 1);
        }
        return max;
    }

    public static void main(String[] args) {
        int count = lengthOfLongestSubstring2("abcabcbb");
        System.out.println(count);
    }
}
