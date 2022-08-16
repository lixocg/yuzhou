package com.yuzhou.demo.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
public class FlapMapTest {

    /**
     * 在Stream中我们可以通过flatMap将多维数据打开降维，扁平化处理数据为一维数据。
     * Reactor当然也有这种需求，我们可以使用flatMap和concatMap进行数据的降维处理
     */

    /**
     * flatMap和flatMapSequential的订阅是同时进行的，而concatMap的是有先后顺序的
     * concatMap和flatMapSequential的值是跟源中值顺序相同，其中flatMapSequential是经过后排序，二者输出相同
     * flatMap中的值是交错的，根据事件触发
     */

    @Test
    public void flatMap () throws InterruptedException {
        Flux.just("abcd", "ffzs")
                .flatMap(i -> Flux.fromArray(i.split("")).delayElements(Duration.ofMillis(10)))
                .subscribe(i -> System.out.print("->"+i));
        Thread.sleep(100);
    }

    @Test
    public void flatMapSequential () throws InterruptedException {
        Flux.just("abcd", "ffzs")
                .flatMapSequential(i -> Flux.fromArray(i.split("")).delayElements(Duration.ofMillis(10)))
                .subscribe(i -> System.out.print("->"+i));
        Thread.sleep(100);
    }

    @Test
    public void flatMapIterable () {
        Flux.just("abcd", "ffzs")
                .flatMapIterable(i -> Arrays.asList(i.split("")))
                .subscribe(i -> System.out.print("->"+i));
    }

    @Test
    public void concatMap () throws InterruptedException {
        Flux.just("abcd", "ffzs")
                .concatMap(i -> Flux.fromArray(i.split("")).delayElements(Duration.ofMillis(10)))
                .subscribe(i -> System.out.print("->"+i));
        Thread.sleep(110);
    }

    @Test
    public void concatMapIterable () {
        Flux.just("abcd", "ffzs")
                .concatMapIterable(i -> Arrays.asList(i.split("")))
                .subscribe(i -> System.out.print("->"+i));
    }
}
