package com.yuzhou.netty.demo.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDate;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("client output: " + msg);
        ctx.writeAndFlush("from client: " + LocalDate.now());
//        ctx.writeAndFlush(99L);
    }

    /**
     * 当客户端和服务器端建立连接调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.writeAndFlush(12345L);
        //比较差异
//        ctx.writeAndFlush(12345);
        //在比较差异
//        ctx.writeAndFlush(Unpooled.copiedBuffer("helloworld".toCharArray(), Charset.forName("utf8")));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
