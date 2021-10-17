package com.yuzhou.rmq.common;

import java.io.Serializable;
import java.util.Map;

/**
 * 发送消息
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午6:03
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 6653799783946834606L;

    private String topic;

    /**
     * 消息内容
     */
    private Map<String,String> content;

    /**
     * 延迟时间，ms
     */
    private long delayMs;

    public Map<String, String> getContent() {
        return content;
    }

    public String getTopic() {
        return topic;
    }

    public long getDelayMs() {
        return delayMs;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDelayMs(long delayMs) {
        this.delayMs = delayMs;
    }
}
