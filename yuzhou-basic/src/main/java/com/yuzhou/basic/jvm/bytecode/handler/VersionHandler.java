package com.yuzhou.basic.jvm.bytecode.handler;

import com.yuzhou.basic.jvm.bytecode.type.ClassFile;
import com.yuzhou.basic.jvm.bytecode.type.U2;

import java.nio.ByteBuffer;

public class VersionHandler implements BaseByteCodeHandler{

    @Override
    public int order() {
        return 1;
    }

    @Override
    public void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception {
        U2 minorVersion = new U2(codeBuf.get(),codeBuf.get());
        U2 majorVersion = new U2(codeBuf.get(),codeBuf.get());

        classFile.setMinor_version(minorVersion);
        classFile.setMajor_version(majorVersion);
    }
}
