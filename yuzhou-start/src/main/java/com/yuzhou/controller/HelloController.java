package com.yuzhou.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    @RequestMapping("hello")
    public String hello(){
        return "hello ....";
    }

    @RequestMapping("/hi")
    public Mono<String> hi(){
        return Mono.just("hi...");
    }
}
