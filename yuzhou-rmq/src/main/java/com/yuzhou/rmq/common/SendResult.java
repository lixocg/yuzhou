package com.yuzhou.rmq.common;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-17
 * Time: 下午8:06
 */
public class SendResult implements Serializable {

    private static final long serialVersionUID = -4015154304320910205L;

    private String msgId;

    private boolean success;

    public SendResult(){}

    public SendResult(String msgId) {
        this.success = true;
        this.msgId = msgId;
    }

    public static SendResult ok(String msgId){
        return new SendResult(msgId);
    }

    public static SendResult notOk(){
        return new SendResult();
    }

    public static SendResult id(long time, long seq) {
        return new SendResult(time + "-" + seq);
    }

    public String getMsgId() {
        return msgId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "SendResult{" +
                "msgId='" + msgId + '\'' +
                '}';
    }
}
