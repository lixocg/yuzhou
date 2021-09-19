package com.yuzhou.rmq.common;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-17
 * Time: 下午8:06
 */
public class SendResult {

    String msgId;

    public SendResult(String msgId) {
        this.msgId = msgId;
    }

    public static SendResult id(long time, long seq) {
        return new SendResult(time + "-" + seq);
    }

    @Override
    public String toString() {
        return "SendResult{" +
                "msgId='" + msgId + '\'' +
                '}';
    }
}
