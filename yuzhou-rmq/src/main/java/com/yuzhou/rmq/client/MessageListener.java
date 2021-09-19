package com.yuzhou.rmq.client;

import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.MessageExt;

import java.util.List;

public interface MessageListener {
    ConsumeStatus consumeMessage(final List<MessageExt> msgs,
                                 final ConsumeContext context);
}