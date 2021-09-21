package com.yuzhou.rmq.remoting;

import com.yuzhou.rmq.utils.MsgIdUtil;
import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午11:37
 */
public class PutResult {

    private static final long serialVersionUID = -4015154304320910205L;

    private String msgId;

    private boolean success;

    private String errorMsg;

    public PutResult() {
    }

    public PutResult(String msgId) {
        this.success = true;
        this.msgId = msgId;
    }


    public static PutResult id(long time, long seq) {
        return new PutResult(MsgIdUtil.id(time, seq));
    }

    public static PutResult err(String errorMsg) {
        PutResult result = new PutResult();
        result.success = false;
        result.errorMsg = errorMsg;
        return result;
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
        return "PutResult{" +
                "msgId='" + msgId + '\'' +
                '}';
    }

}
