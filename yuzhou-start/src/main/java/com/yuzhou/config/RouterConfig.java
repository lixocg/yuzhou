package com.yuzhou.config;

import com.yuzhou.handler.TimeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 配置RouterFunction
 * RouterFunction，顾名思义，路由，相当于@RequestMapping，用来判断什么样的url映射到那个具体的HandlerFunction，输入为请求，
 * 输出为装在Mono里边的 Handlerfunction
 */
@Configuration
public class RouterConfig {

    @Autowired
    private TimeHandler timeHandler;

    @Bean
    public RouterFunction<ServerResponse> timerRouter() {
        return route(GET("/time"), req -> timeHandler.getTime(req))
                .andRoute(GET("/date"), req -> timeHandler.getDate(req))
                .andRoute(GET("/times"),timeHandler::sendTimePerSec)
                ;
    }

}
