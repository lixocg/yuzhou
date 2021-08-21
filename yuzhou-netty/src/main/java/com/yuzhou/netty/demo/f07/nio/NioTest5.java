package com.yuzhou.netty.demo.f07.nio;

import java.nio.ByteBuffer;

public class NioTest5 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        buffer.put((byte)1);
        buffer.putInt(35);
//        buffer.putChar('我');
        buffer.putShort((short)33);
        buffer.putDouble(3.5);

        buffer.flip();

        System.out.println(buffer.get());
        System.out.println(buffer.getInt());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getDouble());
    }
}
