package com.yuzhou.rmq.common;

import java.util.Map;

/**
 * 消费消息
 * User: lixiongcheng
 * Date: 2021-09-17
 * Time: 下午8:06
 */
public class MessageExt {

    private String msgId;

    private String offsetMsgId;

    private Map<String,String> content;

    public String getMsgId() {
        return msgId;
    }

    public String getOffsetMsgId() {
        return offsetMsgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    public void setOffsetMsgId(String offsetMsgId) {
        this.offsetMsgId = offsetMsgId;
    }
}
