package com.yuzhou.algorithm.leecode;

/**
 * 5230. 缀点成线 显示英文描述
 * 在一个 XY 坐标系中有一些点，我们用数组 coordinates 来分别记录它们的坐标，其中 coordinates[i] = [x, y] 表示横坐标为 x、纵坐标为 y 的点。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/check-if-it-is-a-straight-line
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class A1232_checkStraightLine {
    public static boolean checkStraightLine(int[][] coordinates) {
        int x1 = coordinates[0][0];
        int y1 = coordinates[0][1];
        int x2 = coordinates[1][0];
        int y2 = coordinates[1][1];


        for (int i = 0; i < coordinates.length; i++) {
            int y = f(x1, y1, x2, y2, coordinates[i][0]);
            if (y != coordinates[i][1]) {
                return false;
            }
        }
        return true;
    }


    private static int f(int x1, int y1, int x2, int y2, int x) {
        int k = 0;
        if (x2 - x1 == 0) {
            return x1;
        }
        if (y2 - y1 == 0) {
            k = 1;
        }
        k = (y2 - y1) / (x2 - x1);
        return k * (x - x1) + y1;
    }

    public static void main(String[] args) {
        int[][] coordinates = {{-7, -3}, {-7, -1}, {-2, -2}, {0, -8}, {2, -2}, {5, -6}, {5, -5}, {1, 7}};
        System.out.println(checkStraightLine(coordinates));
    }
}