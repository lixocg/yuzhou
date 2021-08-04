package com.yuzhou.log.javaconfig;

import java.io.Serializable;

public class TestVO implements Serializable {
    private String pName;

    private Boolean pSex;

    private Integer pAge;

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Boolean getpSex() {
        return pSex;
    }

    public void setpSex(Boolean pSex) {
        this.pSex = pSex;
    }

    public Integer getpAge() {
        return pAge;
    }

    public void setpAge(Integer pAge) {
        this.pAge = pAge;
    }
}
