package com.yuzhou.rmq.common;

import com.alibaba.fastjson.JSON;
import com.yuzhou.rmq.remoting.ProcessCallback;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-23
 * Time: 下午9:54
 */
public class PullResult implements Serializable {

    private static final long serialVersionUID = 4310158266232486772L;

    private List<MessageExt> messageExts;

    private ProcessCallback processCallback;

    private String topic;

    public List<MessageExt> getMessageExts() {
        return messageExts;
    }

    public void setMessageExts(List<MessageExt> messageExts) {
        this.messageExts = messageExts;
    }

    public ProcessCallback getProcessCallback() {
        return processCallback;
    }

    public void setProcessCallback(ProcessCallback processCallback) {
        this.processCallback = processCallback;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public static Builder builder(){
        return new Builder();
    }

    private PullResult(){}

    public static class Builder{
        private final PullResult pullResult = new PullResult();

        public Builder messageExts(List<MessageExt> messageExts){
            this.pullResult.messageExts = messageExts;
            return this;
        }

        public Builder processCallback(ProcessCallback processCallback){
            this.pullResult.processCallback = processCallback;
            return this;
        }

        public Builder topic(String topic){
            this.pullResult.topic = topic;
            return this;
        }

        public PullResult build(){
            return pullResult;
        }
    }
}
