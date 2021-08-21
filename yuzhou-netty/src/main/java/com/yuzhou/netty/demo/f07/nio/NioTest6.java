package com.yuzhou.netty.demo.f07.nio;

import java.nio.ByteBuffer;
import java.text.MessageFormat;

public class NioTest6 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (byte i = 0; i < buffer.capacity(); i++) {
            buffer.put(i);
        }

        buffer.position(2);
        buffer.limit(6);

        ByteBuffer sliceBuffer = buffer.slice();

        print("原Buffer position={0},limit={1},capacity={2}",buffer.position(),buffer.limit(),buffer.capacity());
        print("新Buffer position={0},limit={1},capacity={2}",sliceBuffer.position(),sliceBuffer.limit(),sliceBuffer.capacity());

        for (byte i = 0; i < sliceBuffer.capacity(); i++) {
            sliceBuffer.put(i, (byte) (sliceBuffer.get() * 20));
        }

        buffer.position(0);
        buffer.limit(10);

        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }

    public static void print(String format, Object... param) {
        System.out.println(MessageFormat.format(format, param));
    }
}
