package com.yuzhou.rmq.common;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-21
 * Time: 下午11:30
 */
public enum MsgReservedKey {
    DELAY("_delay_"),
    RETRY_COUNT("_count_"),
    ;

    private String key;

    MsgReservedKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
