package com.yuzhou.basic.jvm.bytecode.handler;

import com.yuzhou.basic.jvm.bytecode.type.ClassFile;
import com.yuzhou.basic.jvm.bytecode.type.U4;

import java.nio.ByteBuffer;

public class MagicHandler implements BaseByteCodeHandler{

    @Override
    public int order() {
        return 0;
    }

    @Override
    public void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception {
        //连续读取4个字节
        U4 magic = new U4(codeBuf.get(),codeBuf.get(),codeBuf.get(),codeBuf.get());
        classFile.setMagic(magic);

        if(!"0xCAFEBABE".equalsIgnoreCase(classFile.getMagic().toHex())){
            throw new Exception("not a class file");
        }
    }
}
