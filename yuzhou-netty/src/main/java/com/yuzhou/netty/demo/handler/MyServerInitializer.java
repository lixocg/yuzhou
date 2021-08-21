package com.yuzhou.netty.demo.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //当服务器端通过MyServerHandler向客户端写会一个Long类型时，需要outbound编码器将Long类型数据编码为ByteBuf
        pipeline.addLast(new MyLongToByteEncoder());

        //MyServerHandler处理器从客户端接收到Long，前置需要一个inbound解码器将客户端数据先转换成Long
        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyServerHandler());
    }
}
