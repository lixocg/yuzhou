package com.yuzhou.netty.demo.f07.nio;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel 将ByteBuffer 中的数据也不会马上写入到磁盘中，也是先写到pagecache，
 * 但是FileChannel 可以控制写盘的大小，它当然会尽可能写满一块数据块，然后再调用 force() 方法，用于通知操作系统进行及时的刷盘。
 * <p>
 * 所以要想提高FileChannle的效率，必须要了解当前磁盘的数据库大小然后做出写盘大小的调整，
 * 这样效率才会快，从这方面也可以看出来FileChannle在读效率上并没有什么优势。
 */
public class NioTest9 {
    public static void main(String[] args) throws Exception {
        //读取堆外文件
        RandomAccessFile randomAccessFile = new RandomAccessFile(
                "/Volumes/Doer/ICode/Java/moddle/src/main/java/com/netty/f07/nio/NioTest2.txt", "rw"
        );

        //建立内存映射
        MappedByteBuffer mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 5);
        //修改内存数据,NioTest2.txt文件同时也会做出相应修改
        mappedByteBuffer.put(2, (byte) 'a');
        mappedByteBuffer.put(4, (byte) 'b');

        randomAccessFile.close();
    }

    @Test
    public void test01() {
        File file = new File("/Volumes/Doer/ICode/Java/moddle/src/main/java/com/netty/f07/nio/NioTest3.txt");
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            File file = new File("/Users/lixiongcheng/storetest/a");
            //先按照“rw”访问模式打开文件,如果这个文件还不存在，RandomAccessFile的构造方法会创建该文件
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);

            // 写
            byte[] data = "hell".getBytes();
            //从当前 mmap 指针的位置写入 4b 的数据
            mappedByteBuffer.put(data);


            //指定 position 写入 4b 的数据
            ByteBuffer subBuffer = mappedByteBuffer.slice();
            int position = 8;
            subBuffer.position(position);
            subBuffer.put(data);

//
//            //读
//            byte[] data = new byte[4];
//            int position = 8;
//            //从当前 mmap 指针的位置读取 4b 的数据
//            mappedByteBuffer.get(data);
//            //指定 position 读取 4b 的数据
//            ByteBuffer subBuffer = mappedByteBuffer.slice();
//            subBuffer.position(position);
//            subBuffer.get(data);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test3() {
        try {
            File file = new File("/Users/lixiongcheng/storetest/a");
            //先按照“rw”访问模式打开文件,如果这个文件还不存在，RandomAccessFile的构造方法会创建该文件
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);

            //读
            byte[] data = new byte[4];
            int position = 8;
            //从当前 mmap 指针的位置读取 4b 的数据
            mappedByteBuffer.get(data);
            //指定 position 读取 4b 的数据
            ByteBuffer subBuffer = mappedByteBuffer.slice();
            subBuffer.position(position);
            subBuffer.get(data);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
