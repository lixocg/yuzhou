package com.yuzhou.netty.demo.f06.netty2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class TestClientHandler extends SimpleChannelInboundHandler<MyDataInfo2.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo2.MyMessage msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int nextInt = new Random().nextInt(3);

        MyDataInfo2.MyMessage myMessage = null;
        if(nextInt == 1){
            myMessage = MyDataInfo2.MyMessage.newBuilder()
                    .setDataType(MyDataInfo2.MyMessage.DataType.PersonType)
                    .setPerson(
                            MyDataInfo2.Person.newBuilder()
                                    .setName("张三")
                                    .setAge(20)
                                    .setAddress("北京")
                                    .build()
                    ).build();
        }else if(nextInt == 2){
            myMessage =  MyDataInfo2.MyMessage.newBuilder()
                    .setDataType(MyDataInfo2.MyMessage.DataType.DogType)
                    .setDog(
                            MyDataInfo2.Dog.newBuilder()
                                    .setName("张狗")
                                    .setAge(2)
                                    .build()
                    ).build();
        }else{
            myMessage =  MyDataInfo2.MyMessage.newBuilder()
                    .setDataType(MyDataInfo2.MyMessage.DataType.CatType)
                    .setCat(
                            MyDataInfo2.Cat.newBuilder()
                                    .setName("张猫")
                                    .setCity("昆明")
                                    .build()
                    ).build();
        }




        ctx.channel().writeAndFlush(myMessage);
    }
}
