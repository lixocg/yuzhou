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
    void handle(PullResult pullResult);

    boolean isBusy();

    boolean setBusy();

    boolean setIdle();

    ExecutorService executorService();
}
