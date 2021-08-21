package com.yuzhou.netty.demo.f03;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.forEach(ch -> {
            if (ch != channel) {
                ch.writeAndFlush(ch.remoteAddress() + " 发送消息: " + msg + "\n");
            }else {
                ch.writeAndFlush("[自己] " + msg + "\n");
            }
        });
    }

    /**
     * 客户端与服务器端建立连接时调用
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //获取连接
        Channel channel = ctx.channel();
        //广播消息到已经连接客户端
        channelGroup.writeAndFlush("[服务器]- " + channel.remoteAddress() + "加入\n");
        //把当前连接放入channelGroup
        channelGroup.add(channel);
    }

    /**
     * 客户端失去连接的调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //获取连接
        Channel channel = ctx.channel();
        //广播消息到已经连接客户端
        channelGroup.writeAndFlush("[服务器]- " + channel.remoteAddress() + "离开\n");
        //移除连接，netty会自动移动
//        channelGroup.remove(channel);

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 上线\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 下线\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
