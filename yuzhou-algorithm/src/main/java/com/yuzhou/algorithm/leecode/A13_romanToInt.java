package com.yuzhou.algorithm.leecode;

import java.util.HashMap;
import java.util.Map;

class A13_romanToInt {
    public static int romanToInt(String s) {
        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);

        int nextVal = map.get(s.charAt(s.length() - 1));
        int ans = nextVal;
        for (int i = s.length() - 2; i > -1; i--) {
            Integer curVal = map.get(s.charAt(i));
            if (curVal < nextVal) {
                ans -= curVal;
            } else {
                ans += curVal;
            }
            nextVal = curVal;
        }


        return ans;
    }

    public static void main(String[] args) {
        System.out.println(romanToInt("LVIII"));
    }
}