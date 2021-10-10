package com.yuzhou.rmq.client;

import com.yuzhou.rmq.common.ConsumeFromWhere;
import com.yuzhou.rmq.common.Message;
import com.yuzhou.rmq.exception.IllegalMsgException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 客户端配置
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 下午11:20
 */
public class ClientConfig {

    /**
     * pending空闲时间，超过此时间的pending消息将被处理
     */
    private static final long PENDING_IDLE_MS = 20*1000;


    public long pendingIdleMs(){
        return PENDING_IDLE_MS;
    }

    public ConsumeFromWhere getConsumeFromWhere(){return null;};

    public enum ReservedKey{
        DELAY_KEY("_delay","延迟消息标记"),
        TAG_KEY("_tag","消息过滤标记"),
        RETRY_COUNT_KEY("_count","重试次数标记"),
        ;

         public String val;
         public String desc;

        ReservedKey(String val,String desc){
            this.val = val;
            this.desc = desc;
        }
    }


    protected void checkMsg(Message message) {
        if (message == null) {
            throw new IllegalMsgException("消息为空");
        }

        if (StringUtils.isBlank(message.getTopic())) {
            throw new IllegalMsgException("消息topic为空");
        }

        checkMsg(message.getContent());
    }

    protected void checkMsg(Map<String, String> msg) {
        if (msg == null || msg.size() == 0) {
            throw new IllegalMsgException("消息为空");
        }

        for (ReservedKey reservedKey : ReservedKey.values()) {
            if (msg.containsKey(reservedKey.val)) {
                throw new IllegalMsgException(String.format("%s为保留字，请更换", reservedKey.val));
            }
        }
    }
}
