package com.yuzhou.netty.demo.zorecopy;


import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;

public class NewIOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8899));
        socketChannel.configureBlocking(true);

        String fileName = "/Users/lixiongcheng/Downloads/netty/49_零拷贝深入剖析及用户空间与内核空间切换方式.mp4";
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long start = System.currentTimeMillis();
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println(MessageFormat.format("发送总字节数:{0},耗时:{1}", transferCount, (System.currentTimeMillis() - start)));
    }
}
