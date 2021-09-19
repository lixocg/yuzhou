package com.yuzhou.rmq.client;

import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.Message;

import java.util.List;

public interface MessageListener {
    ConsumeStatus consumeMessage(final List<Message> msgs,
                                 final ConsumeContext context);
}