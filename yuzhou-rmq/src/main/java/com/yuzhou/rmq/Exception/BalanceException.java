package com.yuzhou.rmq.exception;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-21
 * Time: 下午11:15
 */
public class BalanceException extends RuntimeException{
    private String errMsg;

    public BalanceException(){
        super();
    }

    public BalanceException(String errMsg){
        super(errMsg);
    }
}
