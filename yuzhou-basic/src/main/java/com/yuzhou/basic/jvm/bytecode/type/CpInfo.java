package com.yuzhou.basic.jvm.bytecode.type;

import com.yuzhou.basic.jvm.bytecode.handler.ConstantInfoHandler;

public abstract class CpInfo implements ConstantInfoHandler {
    private U1 tag;

    public CpInfo(U1 tag){
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "tag = " + tag;
    }
}
