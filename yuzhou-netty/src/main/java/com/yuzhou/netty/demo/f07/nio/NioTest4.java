package com.yuzhou.netty.demo.f07.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest4 {
    public static void main(String[] args) throws Exception{
        FileInputStream inputStream = new FileInputStream("/Volumes/Doer/ICode/Java/moddle/src/main/java/com/netty/f07/nio/NioTest2.txt");
        FileOutputStream outputStream = new FileOutputStream("/Volumes/Doer/ICode/Java/moddle/src/main/java/com/netty/f07/nio/output.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(7);

        while (true){
//            buffer.clear();//关键

            int read = inputChannel.read(buffer);
            System.out.println("写入buffer字节数: " + read);
            if(-1 == read){
                break;
            }
            buffer.flip();

            outputChannel.write(buffer);
        }

        inputStream.close();
        outputStream.close();

    }
}
