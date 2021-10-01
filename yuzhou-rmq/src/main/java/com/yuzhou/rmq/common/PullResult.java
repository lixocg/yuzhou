package com.yuzhou.rmq.common;

import com.yuzhou.rmq.factory.ProcessCallback;

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

    private String group;

    public List<MessageExt> messageExts() {
        return messageExts;
    }


    public ProcessCallback processCallback() {
        return processCallback;
    }

    public String topic() {
        return topic;
    }

    public String group(){
        return group;
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

        public Builder group(String group){
            this.pullResult.group = group;
            return this;
        }

        public PullResult build(){
            return pullResult;
        }
    }
}
