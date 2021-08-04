package com.yuzhou.log;

import com.yuzhou.log.annotation.EnableCommonLog;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 启用统一日志,用于xml配置
 *
 * @author lixiongcheng
 * @see EnableCommonLog
 */
@Configuration
@ComponentScan
@EnableCommonLog
public class EnableLog {

}
