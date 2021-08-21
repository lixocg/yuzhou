package com.yuzhou.netty.demo.f07.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Set;

public class NioTest11 {
    public static void main(String[] args) throws Exception {
        int[] ports = new int[5];
        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;

        //获取一个Selector对象
        Selector selector = Selector.open();

        for (int i = 0; i < ports.length; i++) {
            //获取一个ServerChannelSocket对象
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);//非阻塞

            //获取socket对象
            ServerSocket serverSocket = serverSocketChannel.socket();

            //绑定端口
            serverSocket.bind(new InetSocketAddress(ports[i]));

            //将ServerSocketChannel注册到Selector上，事件为接受连接
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            print("监听端口: {0}", ports[i]);
        }

        //处理请求，和Java IO编程模型一样，一个while循环
        while (true) {
            //select方法是一个阻塞方法，直到注册到它的那些通道所监听的事件(SelectionKey)发生
            int numbers = selector.select();
            print("numbers: {0}", numbers);

            //select关注事件集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            print("selectionKeys = {0}", selectionKeys);

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //处理SelectionKey.OP_ACCEPT事件
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    //建立好连接，在注册一个读取数据事件
                    socketChannel.register(selector, SelectionKey.OP_READ);

                    //移除
                    iterator.remove();

                    print("客户端建立连接: " + socketChannel);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    int bytesRead = 0;
                    while (true) {
                        ByteBuffer buffer = ByteBuffer.allocate(512);
                        //读写一定要clear
                        buffer.clear();

                        int read = socketChannel.read(buffer);
                        if (read <= 0) {
                            break;
                        }

                        buffer.flip();

                        socketChannel.write(buffer);

                        bytesRead += read;

                    }

                    print("读取: {0},来自于={1}", bytesRead, socketChannel);

                    //使用完记得删除
                    iterator.remove();

                }
            }
        }
    }

    public static void print(String format, Object... param) {
        System.out.println(MessageFormat.format(format, param));
    }
}
