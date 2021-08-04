package com.yuzhou.log.javaconfig;

import com.yuzhou.log.annotation.EnableCommonLog;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.yuzhou.log")
@EnableCommonLog(isPrintConsume = true)
public class SpringConfig {
}
