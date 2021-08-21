package com.yuzhou.netty.demo.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //当客户通过MyClientHandler向客户端写一个Long类型时，需要outbound编码器将Long类型数据编码为ByteBuf
        pipeline.addLast(new MyLongToByteEncoder());

        //MyClientHandler处理器从服务器接收到Long，前置需要一个inbound解码器将客户端数据先转换成Long
        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyClientHandler());
    }
}
