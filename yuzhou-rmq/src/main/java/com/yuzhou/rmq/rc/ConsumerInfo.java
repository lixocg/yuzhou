package com.yuzhou.rmq.rc;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-10
 * Time: 下午3:21
 */
public class ConsumerInfo {
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
