package com.yuzhou.netty.demo.zorecopy;


import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class OldIOClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8899);
        //指定一个大文件
//        String fileName = "/Users/lixiongcheng/Downloads/netty/49_零拷贝深入剖析及用户空间与内核空间切换方式.mp4";
        String fileName = "/Users/pub/Aliworkbench-20180709-1137-9.04.02.dmg";
        InputStream inputStream = new FileInputStream(fileName);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4096];
        long readCount;
        long tocalCount = 0;

        long start = System.currentTimeMillis();

        while ((readCount = inputStream.read(buffer)) >= 0) {
            tocalCount += readCount;
            TimeUnit.MILLISECONDS.sleep(1);
            dataOutputStream.write(buffer);
        }

        System.out.println(MessageFormat.format("发送总字节数:{0},耗时:{1}", tocalCount, System.currentTimeMillis() - start));
        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }
}
