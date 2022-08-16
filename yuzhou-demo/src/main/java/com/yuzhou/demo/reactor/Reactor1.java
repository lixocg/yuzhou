package com.yuzhou.demo.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Reactor1 {

    @Test
    public void testData() {
        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::println);
        System.out.println();
        Mono.just(1).subscribe(System.out::println);
    }

    @Test
    public void testZip() throws InterruptedException {
        //zip
        String desc = "Zip two sources together, that is to say wait for all the sources to emit one element and combine these elements once into a Tuple2.";
        Flux<String> stringFlux = Flux.fromArray(desc.split("\\s+"));

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux.zip(stringFlux,Flux.interval(Duration.ofMillis(200)))
                .subscribe(t -> System.out.println(t.getT1()), null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testSyncToAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Mono.fromCallable(()->getSyncString())
                .subscribeOn(Schedulers.elastic())
                .subscribe(System.out::println,null,latch::countDown);

        latch.await(10,TimeUnit.SECONDS);
    }

    private String getSyncString(){
        try{
            TimeUnit.SECONDS.sleep(6);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Hello Reactor!!";
    }
}
