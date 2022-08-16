package com.yuzhou.demo.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Comparator;

@Slf4j
public class MergeTest {

    /**
     * 由于业务需求有的时候需要将多个数据源进行合并，Reactor提供了concat方法和merge方法：
     * concat是合并的flux，按照顺序分别运行，flux1运行完成以后再运行flux2
     * merge是同时运行，根据时间先后运行
     */


    private Flux<Integer> flux1() {
        return Flux.range(1,4);
    }

    private Flux<Integer> flux2() {
        return Flux.range(5,8);
    }


    private Flux<String> hotFlux1() {
        return flux1().map(i-> "[1]"+i).delayElements(Duration.ofMillis(10));
    }

    private Flux<String> hotFlux2() {
        return flux2().map(i-> "[2]"+i).delayElements(Duration.ofMillis(4));
    }


    @Test
    public void concatTest() throws InterruptedException {

        Flux.concat(hotFlux1(), hotFlux2())
                .subscribe(i -> System.out.print("->"+i));

        Thread.sleep(200);
    }

    @Test
    public void concatWithTest () {
        flux1().concatWith(flux2())
                .log()
                .subscribe();
    }


    @Test
    public void mergeTest() throws InterruptedException {

        Flux.merge(hotFlux1(), hotFlux2())
                .subscribe(i -> System.out.print("->"+i));

        Thread.sleep(200);
    }


    @Test
    public void mergeWithTest() throws InterruptedException {

        hotFlux1().mergeWith(hotFlux2())
                .subscribe(i -> System.out.print("->"+i));

        Thread.sleep(200);
    }

    @Test
    public void mergeSequentialTest() throws InterruptedException {
        Flux.mergeSequential(hotFlux1(), hotFlux2())
                .subscribe(i -> System.out.print("->"+i));

        Thread.sleep(200);
    }

    @Test
    public void mergeOrderedTest() throws InterruptedException {

        Flux.mergeOrdered(Comparator.reverseOrder(), hotFlux1(), hotFlux2())
                .subscribe(i -> System.out.print("->"+i));

        Thread.sleep(200);
    }

    @Test
    public void combineLatestTest() throws InterruptedException {

        Flux.combineLatest(hotFlux1(), hotFlux2(), (v1, v2) -> v1 + ":" + v2)
                .subscribe(i -> System.out.print("->"+i));

        Thread.sleep(200);
    }



}
