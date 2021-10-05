package com.yuzhou.rmq.consumer.handler;

import com.yuzhou.rmq.common.PullResult;

import java.util.concurrent.ExecutorService;

/**
 * 消息处理器
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:03
 */
public interface MsgHandler{

    /**
     * 消息处理，消费回调
     * @param pullResult
     */
    void handle(PullResult pullResult);

    /**
     * 处理器线程池是否繁忙
     * @return
     */
    boolean isBusy();

    /**
     * 标记线程池处于繁忙状态
     * @return
     */
    boolean markBusy();

    /**
     * 标记线程池处于空闲状态
     * @return
     */
    boolean markIdle();

    /**
     * 线程池服务
     * @return
     */
    ExecutorService executorService();
}
