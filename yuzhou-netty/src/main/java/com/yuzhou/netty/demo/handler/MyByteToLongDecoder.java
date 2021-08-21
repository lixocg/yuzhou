package com.yuzhou.netty.demo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义inbound解码器，将客户端发来的字节数据(ByteBuf)转换成Long类型
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("decode invoked!");

        System.out.println(in.readableBytes());
        //Long 占8的字节
        if(in.readableBytes() >= 8){
            out.add(in.readLong());
        }
    }
}
