package com.yuzhou.demo.reactor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple3;

@Slf4j
public class ZipTest {

    /**
     * 处理业务的时候一定有这样的需求：将多个源数据压缩成一个，Reactor提供了zip和zipWith方法可以做到这一点。
     *
     * zip和zipwith有些不同：
     * zip可以一次合并多个源
     * zipWiht一次只能合并两个
     * zipWiht支持prefectch
     * */


    private Flux<String> name () {
        return Flux.just("ffzs", "dz", "sleepycat");
    }

    private Flux<Integer> age () {
        return Flux.just(12, 22, 32);
    }

    private Flux<Integer> salary () {
        return Flux.just(10000, 20000, 30000);
    }

    @Data
    @AllArgsConstructor
    static class Employee {
        String name;
        Integer age;
        Integer salary;
    }

    @Data
    @AllArgsConstructor
    static class User {
        String name;
        Integer age;
    }


    /**
     * 多个源压缩到一起，等待所有源发出一个元素之后，将这些元素进行组合
     * 如果是输入publisher会将多个源自动转化为tuple类型
     */
    @Test
    public void zipTest () {
        Flux<Tuple3<String, Integer, Integer>> flux = Flux.zip(name(), age(), salary());
        flux.subscribe(i -> log.info(i.toString()));
    }

    /**
     * tuple 通过getT1这类方法获取数据
     */
    @Test
    public void zipTest1 () {
        Flux<Tuple3<String, Integer, Integer>> flux = Flux.zip(name(), age(), salary());
        Flux<Employee> employee = flux.map(tuple -> new Employee(tuple.getT1(), tuple.getT2(), tuple.getT3()));
        employee.subscribe(i -> log.info(i.toString()));
    }

    /**
     * zip中可以直接给出合并器
     */
    @Test
    public void zipCombineTest () {
        Flux<Employee>  flux = Flux.zip(objects -> {
            return new Employee((String)objects[0], (Integer)objects[1], (Integer)objects[2]);
        }, name(), age(), salary());
        flux.subscribe(i -> log.info(i.toString()));
    }


    @Test
    public void zipWithTest () {
        Flux<User> flux = name().zipWith(age(), (name, age) -> new User(name, age));
        flux.subscribe(i -> log.info(i.toString()));
    }


}
