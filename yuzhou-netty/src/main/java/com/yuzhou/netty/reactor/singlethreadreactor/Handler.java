package com.yuzhou.netty.reactor.singlethreadreactor;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

final class Handler implements Runnable {
    final SocketChannel socketChannel;
    final SelectionKey sk;

    ByteBuffer input = ByteBuffer.allocate(10000);
    ByteBuffer output = ByteBuffer.allocate(1000);

    static final int READING = 0;
    static final int SENDING = 1;

    int state = READING;


    Handler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);

        sk = socketChannel.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);

        selector.wakeup();
    }

    boolean inputIsComplete() {
        return true;
    }

    boolean outputIdComplete() {
        return true;
    }

    void process() {
    }

    @Override
    public void run() {
        try {
            if (state == READING) {
                read();
            } else {
                send();
            }
        } catch (Exception e) {

        }
    }

    void send() throws IOException {
        socketChannel.write(output);

        if (outputIdComplete()) {
            sk.cancel();
        }
    }

    void read() throws IOException {
        socketChannel.read(input);

        if (inputIsComplete()) {
            process();

            state = SENDING;

            sk.interestOps(SelectionKey.OP_WRITE);
        }
    }
}