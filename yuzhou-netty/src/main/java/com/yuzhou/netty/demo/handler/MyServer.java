package com.yuzhou.netty.demo.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务端输出:
 * decode invoked!
 * 8
 * /127.0.0.1:51217 , 12345
 * encode invoked!
 * 54321
 *
 * 客户端输出:
 * encode invoked!
 * 12345
 * decode invoked!
 * 8
 * localhost/127.0.0.1:8899
 * client output: 54321
 *
 * 运行结果分析:
 * 1.客户端和服务端建立连接，调用channelActive，向服务器端写入数据12345L
 * 此时客户端先调用出站解码器MyLongToByteEncoder处理器将Long类型数转换成ByteBuf
 * 客户端打印：
 * encode invoked!
 * 12345
 *
 *
 * 2.服务器端接收到字节数据，需要向将字节数据转成Long类型数据，调用入站编码器MyByteToLongDecoder
 * decode invoked!
 * 8
 *
 * 3.紧着着服务端可以对Long类型数据做处理，调用入站处理器MyServerHandler
 * 打印
 * /127.0.0.1:51217 , 12345
 * 并向客户端写出数据，此时调用出站编码器MyLongToByteEncoder，将Long类型数据转换成字节数据
 * encode invoked!
 * 54321
 *
 * 4.客户端接到服务器端发来的数据，将调用入站解码器MyByteToLongDecoder，将字节数据转换成Long类型数据，在调用入站处理器MyClientHandler处理Long类型数据
 * decode invoked!
 * 8
 * localhost/127.0.0.1:8899
 * client output: 54321
 * 接着向客户端写出当前时间，显示Date类型，客户端没有入站处理器可处理。
 *
 */

/**
 * 自定义Long类型处理器
 */
public class MyServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,childGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
