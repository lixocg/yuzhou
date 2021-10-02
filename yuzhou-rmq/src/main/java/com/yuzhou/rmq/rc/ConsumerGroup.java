package com.yuzhou.rmq.rc;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-02
 * Time: 上午10:46
 */
public class ConsumerGroup implements Serializable {

    private static final long serialVersionUID = 3466026115782008005L;

    private String groupName;

    private String topic;

    private Set<ConsumerInstance> consumers;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Set<ConsumerInstance> getConsumers() {
        return consumers;
    }

    public void setConsumers(Set<ConsumerInstance> consumers) {
        this.consumers = consumers;
    }
}
