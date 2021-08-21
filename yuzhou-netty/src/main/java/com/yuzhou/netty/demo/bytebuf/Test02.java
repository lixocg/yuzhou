package com.yuzhou.netty.demo.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class Test02 {

    @Test
    public void test1(){
        ByteBuf buf = Unpooled.copiedBuffer("Hello", Charset.forName("utf8"));
        //capacity:15,widx:5,utf8编码英文字符用1个字节，所以写入占5个字节，
        // capacity为15，不同于nio ByteBuffer，Netty ByteBuf是可扩容的
        System.out.println("capacity:"+buf.capacity()+",widx:"+buf.writerIndex());

        //buf.hasArray()判断存储数据是在Heap还是堆外，true为Heap
        if(buf.hasArray()){
            byte[] content = buf.array();
            System.out.println(new String(content,Charset.forName("utf8")));

            System.out.println(buf);

            System.out.println(buf.arrayOffset());

            System.out.println(buf.readerIndex());

            System.out.println(buf.writerIndex());

            System.out.println(buf.capacity());

            //readableBytes = widx - ridx
            int readableBytes = buf.readableBytes();
            System.out.println(readableBytes);


            for(int i = 0; i < readableBytes;i++){
                System.out.println((char)buf.getByte(i));
            }
        }
    }

    @Test
    public void test2(){
        ByteBuf buf = Unpooled.copiedBuffer("哈Hello", Charset.forName("utf-8"));
        //加了一个汉字，一个汉字utf8编码需要3个字节
        //capacity:15+3=18,widx:5+3=8
        System.out.println("capacity:"+buf.capacity()+",widx:"+buf.writerIndex());

        if(buf.hasArray()){
            byte[] content = buf.array();
            System.out.println(new String(content,Charset.forName("utf-8")));

            int readableBytes = buf.readableBytes();
            System.out.println(readableBytes);

            for(int i = 0; i < readableBytes;i++){
                //哈 占三个字节，打印每个字节，哈会输出三个乱码
                System.out.println((char)buf.getByte(i));
            }
        }
    }

    @Test
    public void test3(){
        ByteBuf buf = Unpooled.copiedBuffer("哈Hello", Charset.forName("utf-8"));
        //加了一个汉字，一个汉字utf8编码需要3个字节
        //capacity:15+3=18,widx:5+3=8
        System.out.println("capacity:"+buf.capacity()+",widx:"+buf.writerIndex());

        if(buf.hasArray()){
            byte[] content = buf.array();
            System.out.println(new String(content,Charset.forName("utf-8")));

            int readableBytes = buf.readableBytes();
            System.out.println(readableBytes);

            for(int i = 0; i < readableBytes;i++){
                System.out.println((char)buf.getByte(i));
            }

            //指定字节打印可正常打印 哈
            System.out.println(buf.getCharSequence(0,3,Charset.forName("utf8")));

            System.out.println(buf.getCharSequence(0,readableBytes,Charset.forName("utf8")));
        }
    }

    @Test
    public void test4(){
        try {
            File file = new File("/Users/lixiongcheng/storetest/a");
            //先按照“rw”访问模式打开文件,如果这个文件还不存在，RandomAccessFile的构造方法会创建该文件
            RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");

            FileChannel channel = randomAccessFile.getChannel();
            channel.map(FileChannel.MapMode.READ_WRITE,0,1024);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("hello world".getBytes());
            channel.write(buffer);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
