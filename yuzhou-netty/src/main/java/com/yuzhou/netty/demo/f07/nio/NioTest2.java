package com.yuzhou.netty.demo.f07.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest2 {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("/Volumes/Doer/ICode/Java/moddle/src/main/" +
                "java/com/netty/f07/nio/NioTest2.txt");

        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);

        channel.read(buffer);

        buffer.flip();

        while (buffer.hasRemaining()){
            System.out.println((char) buffer.get());
        }

        fileInputStream.close();
    }
}
