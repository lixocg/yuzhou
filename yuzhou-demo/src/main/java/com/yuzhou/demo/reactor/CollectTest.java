package com.yuzhou.demo.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CollectTest {

    /**
     * 有的时候流数据有需要转化为其他类型数据，同Stream相同，Reactor也有将数据进行收集的方法：
     *
     * collect () : 将数据根据给出的collector进行收集
     * collectList() : 收集收集为list形式
     * collectSortedList(): 数据收集为list并排序，需要给出排序规则
     * collectMap(): 数据收集为Map形式，是key，value形式，因此如果有重复key会覆盖
     * collectMultimap(): 数据收集为Map形式，是key，collection形式，如果有重复key值写在list中
     */


    /**
     * 需要注意的是收集后的为Mono
     */
    @Test
    public void collectTest() {
        Mono<Set<String>> flux = Flux.just("ffzs", "vincent", "tony", "sleepycate")
                .collect(Collectors.toSet())
                .log();

        flux.subscribe();
    }


    /**
     * 直接收集为Mono<List<>>
     */
    @Test
    public void collectListTest() {
        Flux.just("ffzs", "vincent", "tony", "sleepycate")
                .collectList()
                .log()
                .subscribe();
    }

    @Test
    public void collectSortedListTest() {
        Flux.just("ffzs", "vincent", "tony", "sleepycate")
                .collectSortedList(Comparator.comparing(String::length))
                .log()
                .subscribe();
    }



    @Test
    public void collectMapTest() {
        Flux.just("ffzs", "vincent", "tony", "sleepycate")
                //按字符串长度为key，由于 ffzs和tony长度都是4,因此ffzs被tony覆盖掉了
                .collectMap(String::length)
                .log()
                .subscribe();
    }

    @Test
    public void collectMultimapTest() {
        Flux.just("ffzs", "vincent", "tony", "sleepycate")
                //不会被覆盖
                .collectMultimap(String::length)
                .log()
                .subscribe();
    }

}
