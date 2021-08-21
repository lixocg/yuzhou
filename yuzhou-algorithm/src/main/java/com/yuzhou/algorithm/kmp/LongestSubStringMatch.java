package com.yuzhou.algorithm.kmp;

public class LongestSubStringMatch {
    public static String s = "ABCDABDEF";

    public static String p = "DA";

    public static void main(String[] args) {
        System.out.println(violentMatch(s, p));
        System.out.println(kmpSearch(s,p));
    }

    /**
     * 暴力搜索模式串第一次出现的位置
     *
     * @param t 原串
     * @param p 模式串
     * @return
     */
    private static int violentMatch(String t, String p) {
        //指向t串字符index
        int i = 0;
        //指向p串字符index
        int j = 0;
        while (i < t.length() && j < p.length()) {
            if (t.charAt(i) == p.charAt(j)) {
                i++;
                j++;
            } else {
                //回溯t串index,暴力重复匹配
                i = i - j + 1;
                j = 0;
            }

            if (j == p.length()) {
                return i - j;
            }
        }

        return -1;
    }

    private static int kmpSearch(String t, String p) {
        int i = 0;
        int j = 0;

        //模式串前缀后缀匹配表
        int[] next = kmpNext(p);

        while (i < t.length() && j < p.length()) {
            if (j == -1 || t.charAt(i) == p.charAt(j)) {
                i++;
                j++;
            } else {
                j = next[j];
            }


        }

        if (j == p.length()) {
            return i - j;
        }

        return -1;
    }

    /**
     * 前缀后缀最长公共元素长度表
     * <p>
     * next[j]=k 代表索引j之前的字符串中有最大长度为k 的相同前缀后缀。
     * 如果对于值k，已有p0 p1, ..., pk-1 = pj-k pj-k+1, ..., pj-1，相当于next[j] = k
     *
     * @param p
     * @return
     */
    public static int[] kmpNext(String p) {
        int[] next = new int[p.length()];
        next[0] = -1;

        int k = -1;
        int j = 0;

        while (j < p.length() - 1) {
            if (k == -1 || p.charAt(j) == p.charAt(k)) {
                k++;
                j++;
                next[j] = k;
            } else {
                k = next[k];
            }
        }
        return next;
    }
}
