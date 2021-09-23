package com.yuzhou.rmq.remoting;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-23
 * Time: 下午9:58
 */
public interface ProcessCallback {
    void onSuccess();

    void onFail();
}
