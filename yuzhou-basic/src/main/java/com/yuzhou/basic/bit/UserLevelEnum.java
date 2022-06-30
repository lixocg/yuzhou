package com.yuzhou.basic.bit;

import lombok.Getter;

@Getter
public enum UserLevelEnum {
    PU_T(1 << 0, "普通会员"),
    QIN_T(1 << 1, "青铜会员"),
    BAI_Y(1 << 2, "白银会员"),
    HUANG_J(1 << 3, "黄金会员"),
    ZUAN_S(1 << 4, "钻石会员"),
    SHUANG_Z(1 << 5, "双钻会员"),
    HEI_J(1 << 6, "黑金会员"),
    ;

    UserLevelEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;

    private String desc;

}
