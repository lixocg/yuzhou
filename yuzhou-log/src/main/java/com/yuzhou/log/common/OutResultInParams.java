package com.yuzhou.log.common;

import java.io.Serializable;
import java.util.Map;

/**
 * 日志参数对象
 * @author xiongcheng.lxch
 * @see com.yuzhou.log.log.LogService
 */
public class OutResultInParams implements Serializable {
    private String errorMsg;

    private String errorCode;

    private Map<String, Object> outDefineParams;

    private Map<String, Object> inDefineParams;

    private Boolean success;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Map<String, Object> getOutDefineParams() {
        return outDefineParams;
    }

    public void setOutDefineParams(Map<String, Object> outDefineParams) {
        this.outDefineParams = outDefineParams;
    }

    public Map<String, Object> getInDefineParams() {
        return inDefineParams;
    }

    public void setInDefineParams(Map<String, Object> inDefineParams) {
        this.inDefineParams = inDefineParams;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
