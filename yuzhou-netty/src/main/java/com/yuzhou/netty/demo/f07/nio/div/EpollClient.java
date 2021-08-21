package com.yuzhou.netty.demo.f07.nio.div;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class EpollClient {
    public static void main(String[] args) throws IOException {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8000));

            ByteBuffer writeBuffer = ByteBuffer.allocate(32);
            ByteBuffer readBuffer = ByteBuffer.allocate(32);
            byte[] buf = new byte[32];
            Random r = new Random();
            int d = 0;

            d = r.nextInt(1000);
            System.out.println(d);
            writeBuffer.put(String.valueOf(d).getBytes());
            writeBuffer.flip();
            socketChannel.write(writeBuffer);

            socketChannel.read(readBuffer);
            readBuffer.flip();
            System.out.println(new String(readBuffer.array()));

            // 这样的客户端是个大麻烦，说好了要发送数据，结果睡起觉来了
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            writeBuffer.clear();
            d = r.nextInt(10);
            System.out.println(d);
            writeBuffer.put(String.valueOf(d).getBytes());
            writeBuffer.flip();
            socketChannel.write(writeBuffer);

            readBuffer.clear();
            socketChannel.read(readBuffer);
            readBuffer.flip();
            readBuffer.get(buf, 0, readBuffer.remaining());
            System.out.println(new String(buf));

            socketChannel.close();
        } catch (IOException e) {
        }
    }
}