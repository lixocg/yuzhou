package com.yuzhou.algorithm.leecode;

class Solution {
    public static int minimumSwap(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return -1;
        }

        if (s1.length() != s2.length()) {
            return -1;
        }

        int ans = 0;
        char[] s1Chars = s1.toCharArray();
        char[] s2Chars = s2.toCharArray();
        for (int i = 0; i < s1Chars.length; i++) {
            if (s1Chars[i] != s2Chars[i]) {
                char temp = s1Chars[i];
                s1Chars[i] = s2Chars[i];
                s2Chars[i] = temp;

                int j = i;
                boolean needCount = true;
                while (j >= 0) {
                    if (s1Chars[j] != s2Chars[j]) {
                        needCount = false;
                    }
                    j--;
                }
                if (needCount) {
                    ans++;
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        String s1 = "xx", s2 = "yy";
        System.out.println(minimumSwap(s1, s2));
    }
}