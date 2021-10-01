package com.yuzhou.rmq.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-01
 * Time: 上午10:50
 */
public class TopicGroup implements Serializable {

    private static final long serialVersionUID = -6506331421828206054L;

    private String consumerGroup;

    /**
     * 主题组
     */
    private String topicGroupName;

    /**
     * 队列stream
     */
    private List<String> streams;

    private List<String> consumers;

    private Map<String,List<String>> consumerStreams;

    private boolean changed;


    public String getTopicGroupName() {
        return topicGroupName;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setTopicGroupName(String topicGroupName) {
        this.topicGroupName = topicGroupName;
    }

    public List<String> getStreams() {
        return streams;
    }

    public void setStreams(List<String> streams) {
        this.streams = streams;
    }


    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public List<String> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<String> consumers) {
        this.consumers = consumers;
    }

    public Map<String, List<String>> getConsumerStreams() {
        return consumerStreams;
    }

    public void setConsumerStreams(Map<String, List<String>> consumerStreams) {
        this.consumerStreams = consumerStreams;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
