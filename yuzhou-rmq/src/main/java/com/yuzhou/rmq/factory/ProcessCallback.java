package com.yuzhou.rmq.factory;

import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.MessageExt;

import java.util.List;

/**
 * 获取到的数据被消费之后的回调
 * User: lixiongcheng
 * Date: 2021-09-23
 * Time: 下午9:58
 */
public interface ProcessCallback {
    void onSuccess(Context context);

    void onFail(Context context);

    class Context{
        private String topic;
        private String group;
        private List<MessageExt> messageExts;
        private MessageListener messageListener;

        public String getTopic() {
            return topic;
        }

        public MessageListener getMessageListener() {
            return messageListener;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public List<MessageExt> getMessageExts() {
            return messageExts;
        }

        public void setMessageExts(List<MessageExt> messageExts) {
            this.messageExts = messageExts;
        }

        public void setMessageListener(MessageListener messageListener) {
            this.messageListener = messageListener;
        }
    }
}
