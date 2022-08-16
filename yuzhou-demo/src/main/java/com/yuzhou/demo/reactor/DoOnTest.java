package com.yuzhou.demo.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class DoOnTest {
    @Test
    public void doOnWithMono () {
        Mono.just("ffzs")
                .map(String::toUpperCase)
                .doOnSubscribe(subscription -> log.info("test do on subscribe"))
                .doOnRequest(longNumber -> log.info("test do on request"))
                .doOnNext(next -> log.info("test do on next1, value is {}", next))
                .map(String::toLowerCase)
                .doOnNext(next -> log.info("test do on next2, value is {}", next))
                .doOnSuccess(success -> log.info("test do on success: {}", success))
                .subscribe();
    }

    @Test
    public void doOnWithFlux () {
        Flux.range(1,10)
                .map(i -> {
                    if (i == 3) throw new RuntimeException("fake a mistake");
                    else return String.valueOf(i);
                })
                .doOnError(error -> log.error("test do on error, error msg is: {}", error.getMessage()))
                .doOnEach(info -> log.info("do on Each: {}", info.get()))
                .doOnComplete(() -> log.info("test do on complete"))  // 因为error没有完成不触发
                .doOnTerminate(() -> log.info("test do on terminate"))  // 无论完成与否，只要终止就触发
                .subscribe();
    }

    @Test
    public void logTest () {
        Flux.range(1,5)
                .map(i -> {
                    if (i == 3) throw new RuntimeException("fake a mistake");
                    else return String.valueOf(i);
                })
                .log()
                .subscribe();
    }


}
