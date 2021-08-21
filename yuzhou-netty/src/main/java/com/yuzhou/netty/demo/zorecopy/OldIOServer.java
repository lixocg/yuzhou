package com.yuzhou.netty.demo.zorecopy;

import com.netty.BaseTest;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OldIOServer extends BaseTest {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(8899);

        while (true){
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            try {
                byte[] byteArray = new byte[4096];

                long totalCount = 0;

                while (true) {
                    int readCount = dataInputStream.read(byteArray, 0, byteArray.length);
                    totalCount += readCount;
                    if(readCount == -1){
                        break;
                    }
                }
                print("接受总字节数:{0}",totalCount);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
