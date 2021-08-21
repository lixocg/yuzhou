package com.yuzhou.netty.reactor.multihandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by lixiongcheng on 2019/9/4.
 */
public class MainReactor implements Runnable {
    public static void main(String[] args) {
        try {
            MainReactor mainReactor= new MainReactor(8899);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final Selector selector;

    final ServerSocketChannel serverSocketChannel;


    static final int DEFAULT_SUB_POOL_SIZE = 10;
    static final ExecutorService subReactorPool = Executors.newFixedThreadPool(DEFAULT_SUB_POOL_SIZE);

    public MainReactor(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);

        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());

        //初始化SubReactor
        selectors = new Selector[10];
        for (int i = 0; i < selectors.length; i++) {
            Selector sel = Selector.open();
            selectors[i] = sel;
            SubReactor subReactor = new SubReactor(selectors[i]);
            subReactorPool.execute(subReactor);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();

                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> iter = selected.iterator();
                while (iter.hasNext()) {
                    dispatch(iter.next());
                }
                selected.clear();
            }
        } catch (Exception e) {

        }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) k.attachment();
        if (r != null) {
            r.run();
        }
    }

    class SubReactor implements Runnable{
        public Selector sel;

        public SubReactor(Selector sel) {
            this.sel = sel;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    sel.select(1000);
                    System.out.println("subreactor loop");
                    Set<SelectionKey> selected = sel.selectedKeys();
                    Iterator<SelectionKey> it = selected.iterator();
                    while (it.hasNext()) {
                        dispatch((it.next()));
                        it.remove();
                    }
                    selected.clear();
                }
            } catch (IOException e) {

            }
        }
    }

    Selector selectors[];
    int next = 0;

    class Acceptor implements Runnable{

        @Override
        public synchronized void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    socketChannel.configureBlocking(false);

                    SelectionKey sk = socketChannel.register(selectors[next], SelectionKey.OP_READ);
                    sk.attach(new Handler(socketChannel, sk));
                    selectors[next].wakeup();
                    if (++next == selectors.length) {
                        next = 0;
                    }
                }
                if (++next == selectors.length) {
                    next = 0;
                }
            } catch (Exception e) {

            }
        }
    }
}






