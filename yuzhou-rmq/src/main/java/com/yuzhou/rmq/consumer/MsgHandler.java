package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.common.PullResult;

/**
 * 消息处理器
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:03
 */
public interface MsgHandler extends Runnable{
    void handle(PullResult pullResult);
}
