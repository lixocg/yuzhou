package com.yuzhou.rmq.common;

public class ConsumeFromWhere {

    public static final ConsumeFromWhere CONSUME_FROM_LAST_OFFSET = new ConsumeFromWhere(-1);

    public static final ConsumeFromWhere CONSUME_FROM_FIRST_OFFSET = new ConsumeFromWhere(0);

    private long pIndex;

    public ConsumeFromWhere(long pIndex) {
        this.pIndex = pIndex;
    }

    @Override
    public String toString() {
        return String.valueOf(pIndex);
    }
}