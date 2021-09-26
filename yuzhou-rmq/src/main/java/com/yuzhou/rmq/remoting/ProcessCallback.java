package com.yuzhou.rmq.remoting;

import com.yuzhou.rmq.common.CountDownLatch2;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-23
 * Time: 下午9:58
 */
public interface ProcessCallback {
    void onSuccess(Context context);

    void onFail(Context context);

    class Context{
       public CountDownLatch2 latch;
    }
}
