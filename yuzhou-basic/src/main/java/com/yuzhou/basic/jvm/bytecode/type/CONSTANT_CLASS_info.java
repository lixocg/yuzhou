package com.yuzhou.basic.jvm.bytecode.type;

import java.nio.ByteBuffer;

public class CONSTANT_CLASS_info extends CpInfo {

    private U2 name_index;

    public CONSTANT_CLASS_info(U1 tag) {
        super(tag);
    }

    @Override
    public void read(ByteBuffer codeBuf) throws Exception {
        this.name_index = new U2(codeBuf.get(), codeBuf.get());
    }
}
