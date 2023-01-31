package com.yuzhou.basic.jvm.bytecode;

import com.yuzhou.basic.jvm.bytecode.type.ClassFile;
import com.yuzhou.basic.jvm.bytecode.util.ClassFileAnalysiser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ClassFileMain {

    public static ByteBuffer readClassFile(String classFilePath) throws Exception {
        File file = new File(classFilePath);
        if (!file.exists()) {
            throw new Exception("file not exists");
        }
        byte[] byteCodeBuf = new byte[4096];
        int length;
        try (InputStream in = new FileInputStream(file)) {
            length = in.read(byteCodeBuf);
        }
        if (length < 1) {
            throw new Exception("not read byte code");
        }
        return ByteBuffer.wrap(byteCodeBuf, 0, length).asReadOnlyBuffer();
    }

    public static void main(String[] args) throws Exception {
        String path = "/Users/moddle/workspace/java/yuzhou/yuzhou-basic/target/classes/com/yuzhou/basic/jvm/bytecode/TestFile.class";
        ByteBuffer codeBuf = readClassFile(path);
        ClassFile classFile = ClassFileAnalysiser.analysis(codeBuf);
        System.out.println(classFile.getMagic());
    }
}
