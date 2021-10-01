package com.yuzhou.rmq.client;

/**
 * 客户端配置
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 下午11:20
 */
public class ClientConfig {

    public enum ReservedKey{
        DELAY_KEY("_delay","延迟消息标记"),
        TAG_KEY("_tag","消息过滤标记"),
        RETRY_COUNT_KEY("_count","重试次数标记"),
        ;

         public String val;
         public String desc;

        ReservedKey(String val,String desc){
            this.val = val;
            this.desc = desc;
        }
    }
}
