package com.yuzhou.demo.reactornetty;

import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class HttpServerTest {

    public static void main(String[] args) throws InterruptedException {
        DisposableServer server = HttpServer.create()
                .host("localhost")
                .port(9001)
                .bindNow();

        server.onDispose()
                .block();
    }
}
