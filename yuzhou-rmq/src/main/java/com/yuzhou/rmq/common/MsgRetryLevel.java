package com.yuzhou.rmq.common;

/**
 * 消息重试级别
 * User: lixiongcheng
 * Date: 2021-09-21
 * Time: 下午11:45
 */
public enum MsgRetryLevel {
    COUNT_0(0, 0),
    COUNT_1(1, 10),
    COUNT_2(2, 30),
    COUNT_3(3, 60),
//    COUNT_4(4, 2 * 60),
//    COUNT_5(5, 3 * 60),
//    COUNT_6(6, 4 * 60),
//    COUNT_7(7, 5 * 60),
//    COUNT_8(8, 6 * 60),
//    COUNT_9(9, 7 * 60),
//    COUNT_10(10, 8 * 60),
//    COUNT_11(11, 9 * 60),
//    COUNT_12(12, 10 * 60),
//    COUNT_13(13, 20 * 60),
//    COUNT_14(14, 30 * 60),
//    COUNT_15(15, 60 * 60),
//    COUNT_16(16, 2 * 60 * 60),
    ;

    public static final int MAX_RETRY_COUNT = MsgRetryLevel.values().length;

    public static MsgRetryLevel getByCount(int count) {
        for (MsgRetryLevel msgRetryLevel : MsgRetryLevel.values()) {
            if (msgRetryLevel.getCount() == count) {
                return msgRetryLevel;
            }
        }
        return null;
    }

    /**
     * 重试次数
     */
    private int count;

    /**
     * 距离上一次重试结束，延迟时间，单位秒
     */
    private int delay;

    MsgRetryLevel(int count, int delay) {
        this.count = count;
        this.delay = delay;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
