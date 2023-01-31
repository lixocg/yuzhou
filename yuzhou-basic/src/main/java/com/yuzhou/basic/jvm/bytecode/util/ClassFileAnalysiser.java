package com.yuzhou.basic.jvm.bytecode.util;

import com.yuzhou.basic.jvm.bytecode.handler.BaseByteCodeHandler;
import com.yuzhou.basic.jvm.bytecode.handler.MagicHandler;
import com.yuzhou.basic.jvm.bytecode.handler.VersionHandler;
import com.yuzhou.basic.jvm.bytecode.type.ClassFile;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClassFileAnalysiser {

    private final static List<BaseByteCodeHandler> handlers = new ArrayList<>();

    static {
        handlers.add(new MagicHandler());
        handlers.add(new VersionHandler());

        //排序
        handlers.sort(Comparator.comparingInt(BaseByteCodeHandler::order));
    }

    /**
     * 解析class文件为ClassFile对象
     * @param codeBuf class文件的codeBuf
     * @return ClassFile
     * @throws Exception
     */
    public static ClassFile analysis(ByteBuffer codeBuf) throws Exception {
        //重置指针，从头开始
        codeBuf.position(0);

        ClassFile classFile = new ClassFile();

        for (BaseByteCodeHandler handler : handlers) {
            handler.read(codeBuf, classFile);
        }
        return classFile;
    }
}
