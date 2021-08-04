package com.yuzhou.log.common;

import java.io.Serializable;

public class ResultWrapper implements Serializable {
    private Object result;

    private Throwable throwable;

    public ResultWrapper(){}

    public ResultWrapper(Object result,Throwable throwable){
        this.result = result;
        this.throwable = throwable;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
