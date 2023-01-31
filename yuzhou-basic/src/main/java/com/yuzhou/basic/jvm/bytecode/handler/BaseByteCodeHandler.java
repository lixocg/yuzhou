package com.yuzhou.basic.jvm.bytecode.handler;

import com.yuzhou.basic.jvm.bytecode.type.ClassFile;

import java.nio.ByteBuffer;

public interface BaseByteCodeHandler {

    /**
     * 解释器排序值
     */
    int order();

    /**
     * 读取
     */
    void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception;
}
