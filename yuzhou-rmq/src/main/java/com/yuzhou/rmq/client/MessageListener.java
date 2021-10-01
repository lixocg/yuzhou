package com.yuzhou.rmq.client;

import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.MessageExt;

import java.util.List;

/**
 * 消费者消息监听
 */
public interface MessageListener {

    /**
     * 消息处理
     * @param msgs
     * @param context
     * @return ConsumeStatus 状态
     */
    ConsumeStatus onMessage(final List<MessageExt> msgs,
                                 final ConsumeContext context);

    /**
     * onMessage消息经过最大重试仍然失败消息处理
     * @param msgs 经过最大重试后任然失败
     * @param consumeContext
     */
    void onMaxRetryFailMessage(final List<MessageExt> msgs,final ConsumeContext consumeContext);
}