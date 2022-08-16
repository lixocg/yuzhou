package com.yuzhou.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;


/**
 * 定义HandlerFunction
 * HandlerFunction 相当于Controller中的具体处理方法，输入为请求，输出为装在Mono中的响应：
 */
@Component
public class TimeHandler {

    public Mono<ServerResponse> getTime(ServerRequest serverRequest) {
        return ok().contentType(MediaType.TEXT_PLAIN).body(Mono.just("Now is " + new SimpleDateFormat("HH:mm:ss").format(new Date())), String.class);
    }

    public Mono<ServerResponse> getDate(ServerRequest serverRequest) {
        return ok().contentType(MediaType.TEXT_PLAIN).body(Mono.just("Today is " + new SimpleDateFormat("yyyy-MM-dd").format(new Date())), String.class);
    }

    /**
     * 长轮询
     * 常用技术：
     * 短轮询：利用ajax定期向服务器请求，无论数据是否更新立马返回数据，高并发情况下可能会对服务器和带宽造成压力；
     * 长轮询：利用comet不断向服务器发起请求，服务器将请求暂时挂起，直到有新的数据的时候才返回，相对短轮询减少了请求次数；
     * SSE：服务端推送（Server Send Event），在客户端发起一次请求后会保持该连接，服务器端基于该连接持续向客户端发送数据，从HTML5开始加入。
     * Websocket：这是也是一种保持连接的技术，并且是双向的，从HTML5开始加入，并非完全基于HTTP，适合于频繁和较大流量的双向通讯场景。
     */
    public Mono<ServerResponse>  sendTimePerSec(ServerRequest request){
        //MediaType.TEXT_EVENT_STREAM表示Content-Type为text/event-stream，即SSE；
        return ok().contentType(MediaType.TEXT_EVENT_STREAM)
                .body(
                        Flux.interval(Duration.ofSeconds(1))
                                .map(item->new SimpleDateFormat("HH:mm:ss").format(new Date())),
                        String.class);
    }
}
