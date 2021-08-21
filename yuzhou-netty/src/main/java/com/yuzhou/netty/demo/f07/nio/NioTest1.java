package com.yuzhou.netty.demo.f07.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;
import java.util.Random;

public class NioTest1 {
    public static void main(String[] args){
        IntBuffer buffer = IntBuffer.allocate(10);

        Random random = new SecureRandom();

        for(int i = 0; i < buffer.capacity(); i++){
            buffer.put(random.nextInt(20));
        }

        buffer.flip();

        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }

    }
}
