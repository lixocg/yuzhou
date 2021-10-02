package com.yuzhou.rmq.exception;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-21
 * Time: 下午11:15
 */
public class RmqException extends RuntimeException{
    private String errMsg;

    public RmqException(){
        super();
    }

    public RmqException(String errMsg){
        super(errMsg);
    }
}
