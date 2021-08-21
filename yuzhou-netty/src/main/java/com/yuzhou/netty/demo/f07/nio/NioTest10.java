package com.yuzhou.netty.demo.f07.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;
import java.util.Arrays;

public class NioTest10 {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8899);
        serverSocketChannel.socket().bind(address);

        int messageLength = 2 + 3 + 4;

        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate(2);
        buffers[1] = ByteBuffer.allocate(3);
        buffers[2] = ByteBuffer.allocate(4);

        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true) {
            int bytesRead = 0;
            while (bytesRead < messageLength) {
                long r = socketChannel.read(buffers);
                bytesRead += r;

                print("bytesRead: {0}", bytesRead);

                Arrays.asList(buffers).stream().map(buffer -> "position=" + buffer.position() + ",limit=" + buffer.limit())
                        .forEach(System.out::println);
            }

            Arrays.asList(buffers).forEach(buffer -> buffer.flip());

            int bytesWriten = 0;
            while (bytesWriten < messageLength) {
                long w = socketChannel.write(buffers);
                bytesWriten += w;
            }

            Arrays.asList(buffers).forEach(buffer -> buffer.clear());

             print("bytesRead={0},bytesWritten={1},messageLength={2}",bytesRead,bytesWriten,messageLength);

        }
    }

    public static void print(String format, Object... param) {
        System.out.println(MessageFormat.format(format, param));
    }
}
