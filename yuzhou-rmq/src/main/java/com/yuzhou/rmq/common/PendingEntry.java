package com.yuzhou.rmq.common;

import java.io.Serializable;

/**
 * pending消息实体
 * User: lixiongcheng
 * Date: 2021-10-02
 * Time: 下午4:56
 */
public class PendingEntry implements Serializable {

    private static final long serialVersionUID = -515390598736806792L;

    private String offsetMsgId;

    private String consumerName;

    private long idleTime;

    private long deliveredTimes;


    public String getConsumerName() {
        return consumerName;
    }

    public String getOffsetMsgId() {
        return offsetMsgId;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public long getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(long idleTime) {
        this.idleTime = idleTime;
    }

    public long getDeliveredTimes() {
        return deliveredTimes;
    }

    public void setDeliveredTimes(long deliveredTimes) {
        this.deliveredTimes = deliveredTimes;
    }

    public void setOffsetMsgId(String offsetMsgId) {
        this.offsetMsgId = offsetMsgId;
    }
}
