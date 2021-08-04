package com.yuzhou.log.common;

import com.yuzhou.log.util.EagleEyeUtil;
import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;

public class MethodWrapper implements Serializable {
    private MethodInvocation methodInvocation;

    private Long timeConsume;

    private String traceId;

    public MethodWrapper(){

    }

    public MethodWrapper(MethodInvocation methodInvocation,Long timeConsume){
        this(methodInvocation,timeConsume, EagleEyeUtil.getTraceId());
    }

    public MethodWrapper(MethodInvocation methodInvocation,Long timeConsume,String traceId){
        this.methodInvocation = methodInvocation;
        this.timeConsume = timeConsume;
        this.traceId = traceId;
    }

    public MethodInvocation getMethodInvocation() {
        return methodInvocation;
    }

    public void setMethodInvocation(MethodInvocation methodInvocation) {
        this.methodInvocation = methodInvocation;
    }

    public Long getTimeConsume() {
        return timeConsume;
    }

    public void setTimeConsume(Long timeConsume) {
        this.timeConsume = timeConsume;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
