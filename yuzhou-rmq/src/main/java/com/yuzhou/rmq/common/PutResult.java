package com.yuzhou.rmq.common;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午11:37
 */
public class PutResult {

    private static final long serialVersionUID = -4015154304320910205L;

    private String offsetMsgId;

    private boolean success;

    private String errorMsg;

    private PutResult() {
        this.success = true;
    }

    private PutResult(String offsetMsgId) {
        this();
        this.offsetMsgId = offsetMsgId;
    }

    public static PutResult id(String offsetMsgId){
        return new PutResult(offsetMsgId);
    }

    public static PutResult id(long time, long seq) {
        return new PutResult(MsgId.id(time, seq));
    }

    public static PutResult err(String errorMsg) {
        PutResult result = new PutResult();
        result.success = false;
        result.errorMsg = errorMsg;
        return result;
    }

    public String getOffsetMsgId() {
        return offsetMsgId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setOffsetMsgId(String offsetMsgId) {
        this.offsetMsgId = offsetMsgId;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
