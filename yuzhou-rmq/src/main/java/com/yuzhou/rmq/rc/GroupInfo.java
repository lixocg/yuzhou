package com.yuzhou.rmq.rc;

import com.yuzhou.rmq.common.MsgId;
import com.yuzhou.rmq.utils.DateUtil;
import com.yuzhou.rmq.utils.MixUtil;

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

    private long pending;

    private String lastDeliveredId;

    private String lastDeliveredIdT;

    public List<ConsumerInfo> getConsumerInfos() {
        return consumerInfos;
    }

    public String getName() {
        return name;
    }

    public long getPending() {
        return pending;
    }

    public String getLastDeliveredIdT() {
        return lastDeliveredIdT;
    }

    public void setConsumerInfos(List<ConsumerInfo> consumerInfos) {
        this.consumerInfos = consumerInfos;
    }

    public String getLastDeliveredId() {
        return lastDeliveredId;
    }

    public void setLastDeliveredId(String lastDeliveredId) {
        this.lastDeliveredId = lastDeliveredId;
        MsgId msgId = MsgId.split(lastDeliveredId);
        this.lastDeliveredIdT = DateUtil.toStr(msgId.getTime()) + MixUtil.DELIMITER + msgId.getSeq();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPending(long pending) {
        this.pending = pending;
    }

    public void setLastDeliveredIdT(String lastDeliveredIdT) {
        this.lastDeliveredIdT = lastDeliveredIdT;
    }
}
