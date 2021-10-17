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

    private String offsetMsgId;

    private boolean success;

    public SendResult(){}

    public SendResult(String msgId,String offsetMsgId) {
        this.success = true;
        this.msgId = msgId;
        this.offsetMsgId = offsetMsgId;
    }

    public static SendResult ok(String msgId,String offsetMsgId){
        return new SendResult(msgId,offsetMsgId);
    }

    public static SendResult notOk(){
        return new SendResult();
    }

    @Override
    public String toString() {
        return "SendResult{" +
                "msgId='" + msgId + '\'' +
                ", offsetMsgId='" + offsetMsgId + '\'' +
                '}';
    }
}
