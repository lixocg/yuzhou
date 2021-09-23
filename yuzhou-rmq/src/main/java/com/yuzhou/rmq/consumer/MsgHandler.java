package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.common.PullResult;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:03
 */
public interface MsgHandler {
    void handle(PullResult pullResult);
}
