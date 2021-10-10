package com.yuzhou.rmq.rc;

import java.util.List;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-10
 * Time: 下午3:19
 */
public class GroupInfo {
    private String name;

    private List<ConsumerInfo> consumerInfos;

    private long pendingSize;

    private String lastDeliveredId;

    public List<ConsumerInfo> getConsumerInfos() {
        return consumerInfos;
    }

    public String getName() {
        return name;
    }

    public void setConsumerInfos(List<ConsumerInfo> consumerInfos) {
        this.consumerInfos = consumerInfos;
    }

    public long getPendingSize() {
        return pendingSize;
    }

    public void setPendingSize(long pendingSize) {
        this.pendingSize = pendingSize;
    }

    public String getLastDeliveredId() {
        return lastDeliveredId;
    }

    public void setLastDeliveredId(String lastDeliveredId) {
        this.lastDeliveredId = lastDeliveredId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
