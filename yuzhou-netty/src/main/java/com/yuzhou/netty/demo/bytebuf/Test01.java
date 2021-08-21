package com.yuzhou.netty.demo.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.ByteBuffer;

public class Test01 {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            //绝对写入，会改变writerIndex
            byteBuf.writeByte(i);
            System.out.println(byteBuf.readerIndex()+"-"+byteBuf.writerIndex());
        }

        for (int i = 0; i < 10; i++) {
            //相对读取，不会改变readerIndex
            System.out.println(byteBuf.readerIndex()+"-"+byteBuf.writerIndex());
        }

        for (int i = 0; i < 10; i++) {
            //绝对读取，改变readerIndex
            byte b = byteBuf.readByte();
            System.out.println(b+"--"+byteBuf.readerIndex()+"-"+byteBuf.writerIndex());
        }
    }

    @Test
    public void test01(){
        ByteBuffer buffer = ByteBuffer.allocate(10);
//        buffer.putInt(1);
//        buffer.putLong(System.currentTimeMillis());
        buffer.putChar('2');
        buffer.put("ddd".getBytes());
        System.out.println(buffer.position());
    }
}
