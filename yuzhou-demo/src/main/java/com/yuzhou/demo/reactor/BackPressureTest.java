package com.yuzhou.demo.reactor;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

/**
 * 有这样的情况，上游传递到下游的数据需要进行处理，然而上游推送的速度又很快，下游由于资源等原因来不及处理；
 * 如果这时还是通过不限制上游速度的方式推送数据，就会出问题，因此Reactive Streams有两一种处理方式，就是通过request的机制向上游传递信号，
 * 并指定接收数量；通过这种方法将push模型转化为push-pull hybrid，这就是backpressure的用法。
 */
@Slf4j
public class BackPressureTest {

    /**
     * 原始写法:通过构建Subscriber控制request的大小
     */
    @Test
    public void rawBackPressure () {
        Flux<String> flux = Flux.range(1,10)
                .map(String::valueOf)
                .log();

        flux.subscribe(new Subscriber<String>() {
            private int count = 0;
            private Subscription subscription;
            private int requestCount = 2;

            @Override
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                s.request(requestCount);  // 启动
            }

            @SneakyThrows
            @Override
            public void onNext(String s) {
                count++;
                if (count == requestCount) {  // 通过count控制每次request两个元素
                    Thread.sleep(1000);
                    subscription.request(requestCount);
                    count = 0;
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * BaseSubscriber实现backpressure
     */
    @Test
    public void baseBackPressure () {
        Flux<Integer> flux = Flux.range(1,10).log();

        flux.subscribe(new BaseSubscriber<Integer>() {
            private int count = 0;
            private final int requestCount = 2;

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(requestCount);
            }

            @SneakyThrows
            @Override
            protected void hookOnNext(Integer value) {
                count++;
                if (count == requestCount) {  // 通过count控制每次request两个元素
                    Thread.sleep(1000);
                    request(requestCount);
                    count = 0;
                }
            }
        });
    }


    /**
     * 通过flux的limitrate方式实现调整request数量
     */
    @Test
    public void backPressureLimitRate(){
        Flux.range(1,10)
                .log()
                .limitRate(2)
                .subscribe();
    }


}
