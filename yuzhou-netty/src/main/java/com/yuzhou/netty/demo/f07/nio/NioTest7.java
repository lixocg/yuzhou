package com.yuzhou.netty.demo.f07.nio;

import java.nio.ByteBuffer;

public class NioTest7 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        System.out.println("普通Buffer类:" + buffer.getClass());

        ByteBuffer asReadOnlyBuffer = buffer.asReadOnlyBuffer();

        System.out.println("只读Buffer类:" + asReadOnlyBuffer.getClass());
    }
}
