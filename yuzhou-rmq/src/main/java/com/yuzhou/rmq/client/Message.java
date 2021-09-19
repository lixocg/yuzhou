package com.yuzhou.rmq.client;

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

    /**
     * 消息内容
     */
    private Map<String,String> content;

    /**
     * 延迟时间，ms
     */
    private long delayTime;

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }
}
