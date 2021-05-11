package com.yuzhou.algorithm.leecode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 1233. 删除子文件夹
 * 你是一位系统管理员，手里有一份文件夹列表 folder，你的任务是要删除该列表中的所有 子文件夹，并以 任意顺序 返回剩下的文件夹。
 * <p>
 * 我们这样定义「子文件夹」：
 * <p>
 * 如果文件夹 folder[i] 位于另一个文件夹 folder[j] 下，那么 folder[i] 就是 folder[j] 的子文件夹。
 * 文件夹的「路径」是由一个或多个按以下格式串联形成的字符串：
 * <p>
 * / 后跟一个或者多个小写英文字母。
 * 例如，/leetcode 和 /leetcode/problems 都是有效的路径，而空字符串和 / 不是
 */
class A1233_removeSubfolders {
    public static List<String> removeSubfolders(String[] folder) {
        Arrays.sort(folder);

        System.out.println(Arrays.asList(folder));

        List<String> ans = new ArrayList<>();
        String curFold = folder[0];

        for (int i = 1; i < folder.length; i++) {
            if (!subFold(curFold, folder[i])) {
                ans.add(curFold);
                curFold = folder[i];
            }
        }
        ans.add(curFold);
        return ans;
    }

    private static boolean subFold(String ori, String des) {
        if (!des.startsWith(ori)) {
            return false;
        }
        if (des.length() == ori.length()) {
            return false;
        }

        char c = des.charAt(ori.length());
        return c == '/';
    }

    public static void main(String[] args) {
//        System.out.println(subFold("/a/b", "/a/b/v"));
//
        String[] f = {"/a", "/a/b", "/c/d", "/c/d/e", "/c/f"};
        System.out.println(removeSubfolders(f));
    }
}