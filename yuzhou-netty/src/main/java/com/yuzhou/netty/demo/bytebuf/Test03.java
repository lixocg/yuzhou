package com.yuzhou.netty.demo.bytebuf;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by lixiongcheng on 2019/8/19.
 */
public class Test03 {
    @Test
    public void test01(){
        ByteBuffer buffer = ByteBuffer.allocate(16);

        byte[] bytes = "30.29.161.19".getBytes();
        System.out.println(bytes.length);
        buffer.put("30.29.161.19".getBytes());
        buffer.putInt(10911);
        long d = Long.valueOf("000000010A0F592A",16);
//        buffer.putLong(d);

        byte[] array = buffer.array();
        System.out.println(new String(array));

    }

    @Test
    public void test02(){
        System.out.println(8>>>3);
    }
}
