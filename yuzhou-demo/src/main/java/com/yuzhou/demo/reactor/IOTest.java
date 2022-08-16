package com.yuzhou.demo.reactor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class IOTest {
    String filepath = "/Users/moddle/workspace/java/yuzhou/yuzhou-demo/target/classes/info";
    String writePath = "/Users/moddle/workspace/java/yuzhou/yuzhou-demo/target/classes/info1";

    @Test
    public void monoRead(){
        Mono.fromCallable(()-> Files.readAllLines(Paths.get(filepath)))
                .log()
                .subscribe();
    }

    /**
     * 两种方法运行结果都一样，不过第一种方法是全部读取完成之后再处理，
     * 第二种方法是流式处理，文件大的话第一种方法会内存消耗过大，推荐使用第二种方法：
     * @throws IOException
     */
    @Test
    public void FluxRead() throws IOException {
        log.info("--------------------from iterable--------------------------");
        Flux.fromIterable(Files.readAllLines(Paths.get(filepath)))
                .log()
                .subscribe();

        log.info("--------------------from stream--------------------------");
        Flux.fromStream(Files.lines(Paths.get(filepath)))
                .log()
                .subscribe();
    }


    /**
     * 文件有读就有写，写的处理要在subscribe时进行
     *
     * 构建BaseSubscriber对象，通过BufferedWriter，每次onNext的时候将value写入文件
     * 在onComplete的时候flush并关闭
     * @throws IOException
     */
    @Test
    public void baseWrite() throws IOException {

        Flux<String> flux = Flux.fromStream(Files.lines(Paths.get(filepath)))
                .map(String::toUpperCase)
                .log();

        flux.subscribe(new BaseSubscriber<String>() {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(writePath));

            @SneakyThrows
            @Override
            protected void hookOnNext(String value) {
                bw.write(value + "\n");
            }

            @SneakyThrows
            @Override
            protected void hookOnComplete() {
                bw.flush();
                bw.write("**** do flush **** \n");
                bw.close();
            }
        });
    }

    /**
     * 如果文件比较大的话，在关闭的时候flush显然不是很合理
     * 通过计数，写入一定行数之后清理buffer进行flush操作
     * @throws IOException
     */
    @Test
    public void flushWrite() throws IOException {
        Flux<String> flux = Flux.fromStream(Files.lines(Paths.get(filepath)))
                .map(String::toUpperCase)
                .log();

        flux.subscribe(new BaseSubscriber<String>() {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(writePath));
            private int count = 0;
            @SneakyThrows
            @Override
            protected void hookOnNext(String value) {
                count++;
                bw.write(value + "\n");
                if (count % 2 == 0) {       // 设定行数进行清理缓存写入文件
                    bw.write("**** do flush **** \n");
                    bw.flush();
                }
            }
            @SneakyThrows
            @Override
            protected void hookOnComplete() {
                bw.close();
            }
        });
    }


}
