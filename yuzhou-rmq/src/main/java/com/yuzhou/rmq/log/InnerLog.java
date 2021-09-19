package com.yuzhou.rmq.log;

import java.text.MessageFormat;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 上午12:18
 */
public class InnerLog {
    public  void info(String format,Object ... params){
        System.out.println(MessageFormat.format(format,params));
    }
}
