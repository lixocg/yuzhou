package com.yuzhou.rmq.connection;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-02
 * Time: 下午1:41
 */
public class SingleRedisConn implements Connection{

    @Override
    public String host() {
        return "127.0.0.1";
    }

    @Override
    public int ip() {
        return 6379;
    }
}
