package com.yuzhou.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    /** WEBMVC
     * ================================================================================
     * ---- Global Information --------------------------------------------------------
     * > request count                                      20000 (OK=20000  KO=0     )
     * > min response time                                      0 (OK=0      KO=-     )
     * > max response time                                   2426 (OK=2426   KO=-     )
     * > mean response time                                   202 (OK=202    KO=-     )
     * > std deviation                                        411 (OK=411    KO=-     )
     * > response time 50th percentile                          2 (OK=2      KO=-     )
     * > response time 75th percentile                        156 (OK=156    KO=-     )
     * > response time 95th percentile                       1127 (OK=1127   KO=-     )
     * > response time 99th percentile                       1841 (OK=1841   KO=-     )
     * > mean requests/sec                                689.655 (OK=689.655 KO=-     )
     * ---- Response Time Distribution ------------------------------------------------
     * > t < 800 ms                                         17764 ( 89%)
     * > 800 ms < t < 1200 ms                                1359 (  7%)
     * > t > 1200 ms                                          877 (  4%)
     * > failed                                                 0 (  0%)
     * ================================================================================
     * @return
     */
    @RequestMapping("hello")
    public String hello(){
        return "hello ....";
    }

    /** WEBFLUX
     * ---- Global Information --------------------------------------------------------
     * > request count                                      20000 (OK=20000  KO=0     )
     * > min response time                                      0 (OK=0      KO=-     )
     * > max response time                                    578 (OK=578    KO=-     )
     * > mean response time                                     6 (OK=6      KO=-     )
     * > std deviation                                         29 (OK=29     KO=-     )
     * > response time 50th percentile                          2 (OK=2      KO=-     )
     * > response time 75th percentile                          3 (OK=3      KO=-     )
     * > response time 95th percentile                         14 (OK=14     KO=-     )
     * > response time 99th percentile                        123 (OK=123    KO=-     )
     * > mean requests/sec                                714.286 (OK=714.286 KO=-     )
     * ---- Response Time Distribution ------------------------------------------------
     * @return
     */
    @RequestMapping("/hi")
    public Mono<String> hi(){
        return Mono.just("hi...");
    }
}
