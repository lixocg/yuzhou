package com.yuzhou.netty.demo.f06.protobuf;

public class ProtoBufTest {
    public static void main(String[] args) throws Exception{
        //构建protobuf生成对象
        DataInfo.Student student = DataInfo.Student.newBuilder()
                .setName("张三")
                .setAge(20)
                .setAddress("北京")
                .build();
        
        //对象序列化
        byte[] serializeStudent = student.toByteArray();

        //反序列化生成对象
        DataInfo.Student deserializeStudent = DataInfo.Student.parseFrom(serializeStudent);

        System.out.println(deserializeStudent.getName());
        System.out.println(deserializeStudent.getAge());
        System.out.println(deserializeStudent.getAddress());
    }
}
