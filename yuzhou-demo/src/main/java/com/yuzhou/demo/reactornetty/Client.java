package com.yuzhou.demo.reactornetty;

import reactor.core.publisher.Flux;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;

public class Client {

    public static void main(String[] args) {
        HttpClient.create()
                .port(9999)
                .post()
                .uri("/test/world")
                .send(ByteBufFlux.fromString(Flux.just("Hello")))
                .responseContent()
                .aggregate()
                .asString()
                .log("http-client")
                .block();
    }
}
