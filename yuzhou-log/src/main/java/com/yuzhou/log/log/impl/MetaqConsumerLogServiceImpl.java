package com.yuzhou.log.log.impl;

import com.yuzhou.log.common.LogType;
import com.yuzhou.log.common.OutResultInParams;
import com.yuzhou.log.common.ResultWrapper;
import com.yuzhou.log.log.LogServiceAdapter;
import org.aopalliance.intercept.MethodInvocation;

/**
 * metaq consumer日志打印实现
 */
public class MetaqConsumerLogServiceImpl extends LogServiceAdapter {
    private final static String METAQ_SUCCESS_RES = "CONSUME_SUCCESS";
    private final static String METAQ_RETRY_RES = "RECONSUME_LATER";

    @Override
    public LogType logType() {
        return LogType.METAQ_CONSUMER;
    }


    @Override
    public OutResultInParams outResultParams(MethodInvocation methodInvocation, ResultWrapper resultWrapper) {
        OutResultInParams outResultInParams = new OutResultInParams();
        if(throwException(resultWrapper)){
            outResultInParams.setSuccess(Boolean.FALSE);
        }else {
            Boolean success = METAQ_SUCCESS_RES.equalsIgnoreCase(String.valueOf(resultWrapper.getResult())) ? Boolean.TRUE : Boolean.FALSE;
            outResultInParams.setSuccess(success);
        }
        return outResultInParams;
    }

}
