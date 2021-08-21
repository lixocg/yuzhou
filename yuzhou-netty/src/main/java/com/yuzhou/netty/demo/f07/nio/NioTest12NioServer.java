package com.yuzhou.netty.demo.f07.nio;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NioTest12NioServer  {
    private static Map<String, SocketChannel> clientMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

//        ServerSocket serverSocket = serverSocketChannel.socket();
//        serverSocket.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();
//        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        SelectionKey sk = serverSocketChannel.register(selector, 0);
        sk.interestOps(SelectionKey.OP_ACCEPT);

        serverSocketChannel.bind(new InetSocketAddress(8899));

        while (true) {
            try {
                selector.select();
                System.out.println("-------");
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach(selectionKey -> {
                    final SocketChannel client;
                    try {
                        if (selectionKey.isAcceptable()) {
                            ServerSocketChannel socketChannel = (ServerSocketChannel) selectionKey.channel();
                            client = socketChannel.accept();
                            client.configureBlocking(false);

                            client.register(selector, SelectionKey.OP_READ);

                            clientMap.put(UUID.randomUUID().toString(), client);
                        } else if (selectionKey.isReadable()) {
                            client = (SocketChannel) selectionKey.channel();

                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                            int count = client.read(readBuffer);

                            if (count > 0) {
                                readBuffer.flip();

                                Charset charset = Charset.forName("utf-8");
                                String receivedMessage = String.valueOf(charset.decode(readBuffer).array());
                                System.out.println(MessageFormat.format("{0} : {1}", client, receivedMessage));

                                String senderKey = null;
                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                    if (entry.getValue() == client) {
                                        senderKey = entry.getKey();
                                        break;
                                    }
                                }

                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                    SocketChannel value = entry.getValue();

                                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                    writeBuffer.put((senderKey + ":" + receivedMessage).getBytes());

                                    writeBuffer.flip();

                                    value.write(writeBuffer);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                selectionKeys.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
