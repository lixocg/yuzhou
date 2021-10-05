package com.yuzhou.rmq.stat;

import java.io.Serializable;

/**
 * 消费者信息
 * User: lixiongcheng
 * Date: 2021-10-02
 * Time: 下午12:27
 */
public class ConsumerInfo implements Serializable {

    private static final long serialVersionUID = -1334574994030195300L;

    /**
     * 消费者名
     */
    private String name;

    /**
     * 空闲时间，ms
     */
    private long idle;

    /**
     * 未ack列表数量
     */
    private long pending;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIdle() {
        return idle;
    }

    public void setIdle(long idle) {
        this.idle = idle;
    }

    public long getPending() {
        return pending;
    }

    public void setPending(long pending) {
        this.pending = pending;
    }
}
