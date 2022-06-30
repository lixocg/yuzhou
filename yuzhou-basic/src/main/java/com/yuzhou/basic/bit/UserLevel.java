package com.yuzhou.basic.bit;

public class UserLevel {
    /*******************会员常量start************************/
    /**
     * 普通会员
     */
    public static final int PU_T = 1 << 0;

    /**
     * 青铜会员
     */
    public static final int QIN_T = 1 << 1;

    /**
     * 白银会员
     */
    public static final int BAI_Y = 1 << 2;

    /**
     * 黄金会员
     */
    public static final int HUANG_J = 1 << 3;

    /**
     * 钻石会员
     */
    public static final int ZUAN_S = 1 << 4;

    /**
     * 双钻会员
     */
    public static final int SHUANG_Z = 1 << 5;

    /**
     * 黑金会员
     */
    public static final int HEI_J = 1 << 6;

    /*******************会员常量end************************/

    /**
     * 会员类型
     */
    private int value;


}
