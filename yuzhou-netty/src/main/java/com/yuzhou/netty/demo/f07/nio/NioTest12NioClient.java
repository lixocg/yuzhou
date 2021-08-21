package com.yuzhou.netty.demo.f07.nio;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioTest12NioClient {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

            Selector selector = Selector.open();

            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));

            while (true) {
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach(selectionKey -> {
                    if (selectionKey.isConnectable()) {
                        SocketChannel client = (SocketChannel) selectionKey.channel();

                        if (client.isConnectionPending()) {
                            try {
                                client.finishConnect();


                                ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                writeBuffer.put((LocalDateTime.now() + " 连接成功").getBytes());
                                writeBuffer.flip();
                                client.write(writeBuffer);

                                //连接成功之后起一个线程接受键盘输入
                                ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
                                executorService.execute(() -> {
                                    while (true) {
                                        try {
                                            writeBuffer.clear();

                                            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                                            String message = br.readLine();

                                            writeBuffer.put(message.getBytes());
                                            writeBuffer.flip();
                                            client.write(writeBuffer);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            client.register(selector, SelectionKey.OP_READ);
                        } catch (ClosedChannelException e) {
                            e.printStackTrace();
                        }
                    } else if (selectionKey.isReadable()) {
                        SocketChannel client = (SocketChannel) selectionKey.channel();

                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        try {
                            int count = client.read(readBuffer);
                            if (count > 0) {
                                String receivedMsg = new String(readBuffer.array(), 0, count);
                                System.out.println(receivedMsg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
