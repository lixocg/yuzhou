package com.yuzhou.netty.demo.f07.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest3 {
    public static void main(String[] args) throws Exception{
        FileOutputStream fileOutputStream = new FileOutputStream("/Volumes/Doer/ICode/Java/moddle/src/main/java/com/netty/f07/nio/NioTest3.txt");

        //先把数据写到buffer中
        ByteBuffer buffer = ByteBuffer.allocate(512);
        byte[] data = "Hello Wolrd , Welcome".getBytes();
        buffer.put(data);

        buffer.flip();
        //把buffer中数据写到文件中，需要一个channel
        FileChannel channel = fileOutputStream.getChannel();
        channel.write(buffer);

        fileOutputStream.close();
    }
}
