package com.yuzhou.demo.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.stream.Stream;

@Slf4j
public class SchedulerTest {

    /**
     * stream处理并行
     */
    @Test
    public void streamParallel() {
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8).parallel().map(String::valueOf).forEach(log::info);
    }

    /**
     * 在Reactor中，并行执行以及执行的位置由所Scheduler确定 。
     * <p>
     * Schedulers 类有如下几种对上下文操作的静态方法：
     * <p>
     * immediate()：无执行上下文，提交的Runnable将直接在原线程上执行，可以理解没有调度
     * single()：可重用单线程，使用一个线程处理所有请求
     * elastic()： 没有边界的弹性线程池
     * boundedElastic()：有边界弹性线程池，设置线程限制，默认为cpu核心数*10。达到上限后最多可以提交10万个任务。是阻塞线程的方法
     * parallel(): 固定线程数量的并行线程池，线程数量和cpu内核一样多
     * <p>
     * <p>
     * Reactor 提供了两种通过Scheduler切换上下文执行的方法：publishOn和subscribeOn。
     * publishOn在执行顺序中的位置很重要
     * subscribeOn的位置不重要
     */

    @Test
    public void publishOnTest() {
        Flux.range(1, 2)
                .map(i -> {
                    log.info("Map 1, the value map to: {}", i * i);
                    return i * i;
                })
                //切换线程
                .publishOn(Schedulers.single())
                .map(i -> {
                    log.info("Map 2, the value map to: {}", -i);
                    return -i;
                })
                //切换线程
                .publishOn(Schedulers.newParallel("parallel", 4))
                .map(i -> {
                    log.info("Map 3, the value map to: {}", i + 2);
                    return (i + 2) + "";
                })
                .subscribe();
    }

    /**
     * subscribeOn是反向处理，因此先触发parallel，后触发single，因此都是使用的single
     */
    @Test
    public void subscribeOnTest() throws InterruptedException {
        Flux.range(1,2)
                .map(i -> {
                    log.info("Map 1, the value map to: {}", i*i);
                    return i*i;
                })
                .subscribeOn(Schedulers.single())
                .map(i -> {
                    log.info("Map 2, the value map to: {}", -i);
                    return -i;
                })
                .subscribeOn(Schedulers.newParallel("parallel", 4))
                .map(i -> {
                    log.info("Map 3, the value map to  {}", i+2);
                    return (i+2) + "";
                })
                .subscribe();

        Thread.sleep(100);
    }


    /**
     * 优先触发subscribeOn，使用parallel覆盖之后所有过程
     * 当执行完map1后
     * 触发了publishOn，因此该publishOn之后的所有都更换了Schedulers
     * 因此之后的map2,map3都是publishOn的single
     * @throws InterruptedException
     */
    @Test
    public void subscribeOnTest1() throws InterruptedException {
        Flux.range(1,2)
                .map(i -> {
                    log.info("Map 1, the value map to: {}", i*i);
                    return i*i;
                })
                .publishOn(Schedulers.single())
                .map(i -> {
                    log.info("Map 2, the value map to: {}", -i);
                    return -i;
                })
                .subscribeOn(Schedulers.newParallel("parallel", 4))
                .map(i -> {
                    log.info("Map 3, the value map to  {}", i+2);
                    return (i+2) + "";
                })
                .subscribe();

        Thread.sleep(100);
    }


}

