package com.yuzhou.netty.demo.f06.netty2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestServerHandler extends SimpleChannelInboundHandler<MyDataInfo2.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo2.MyMessage msg) throws Exception {
        MyDataInfo2.MyMessage.DataType dataType = msg.getDataType();

        if(dataType == MyDataInfo2.MyMessage.DataType.PersonType){
            MyDataInfo2.Person person = msg.getPerson();
            System.out.println(person.getName());
            System.out.println(person.getAge());
            System.out.println(person.getAddress());
        }else if(dataType == MyDataInfo2.MyMessage.DataType.DogType){
            MyDataInfo2.Dog dog = msg.getDog();
            System.out.println(dog.getName());
            System.out.println(dog.getAge());
        }else {
            MyDataInfo2.Cat cat = msg.getCat();
            System.out.println(cat.getName());
            System.out.println(cat.getCity());
        }


    }
}
