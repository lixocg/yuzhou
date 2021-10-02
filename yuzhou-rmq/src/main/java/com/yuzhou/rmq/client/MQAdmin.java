package com.yuzhou.rmq.client;

import com.yuzhou.rmq.connection.Connection;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-02
 * Time: 下午1:52
 */
public interface MQAdmin {

    void start();

    void shutdown();

    void setConnection(Connection conn);
}
