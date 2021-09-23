package com.yuzhou.rmq.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * 日志框架使用日志
 *
 * @author xiongcheng.lxch
 */
public class InnerLog {
    /**
     * 默认日志格式
     */
    private final static String DEFAULT_PATTERN = "commonlog|%d{yyyy-MM-dd HH:mm:ss.SSS}|%X{EAGLEEYE_TRACE_ID}|%thread|%level|%logger{36}|%m%n";

    /**
     * 日志存储目录
     */
    private final static String DEFAULT_FILE = System.getProperty("user.home", "/home/admin") + "/logs/rmq.log";

    /**
     * 日志滚动文件格式
     */
    private final static String DEFAULT_FILE_PATTERN = DEFAULT_FILE + "-" + "%d{yyyy-MM-dd}.log.%i";

    /**
     * 日志最大保存时间
     */
    private final static int MAX_HISTORY = 7;

    /**
     * 单个日志最大容量
     */
    private final static String MAX_FILE_SIZE = "1GB";

    private static Logger logger = null;

    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        //日志文件策略
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setFile(DEFAULT_FILE);
        rollingFileAppender.setContext(loggerContext);
        rollingFileAppender.setName("rmq");


        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.setFileNamePattern(DEFAULT_FILE_PATTERN);
        rollingPolicy.setMaxHistory(MAX_HISTORY);
        rollingPolicy.setMaxFileSize(FileSize.valueOf(MAX_FILE_SIZE));

        rollingPolicy.start();
        rollingFileAppender.setRollingPolicy(rollingPolicy);


        //设置日志patternEncoder
        PatternLayoutEncoder patternLayoutEncoder = new PatternLayoutEncoder();
        patternLayoutEncoder.setPattern(DEFAULT_PATTERN);
        patternLayoutEncoder.setCharset(Charset.defaultCharset());
        patternLayoutEncoder.setContext(loggerContext);
        patternLayoutEncoder.start();

        rollingFileAppender.setEncoder(patternLayoutEncoder);
        rollingFileAppender.start();
        //打印日志加载详情
        StatusPrinter.print(loggerContext);

        Logger rootLogger = loggerContext.getLogger("InnerLog");
        rootLogger.setLevel(Level.INFO);
        rootLogger.setAdditive(false);
        rootLogger.addAppender(rollingFileAppender);
        logger = rootLogger;

    }

    public static Logger getLogger() {
        return logger;
    }
}
