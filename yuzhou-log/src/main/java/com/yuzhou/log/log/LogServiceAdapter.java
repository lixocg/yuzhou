package com.yuzhou.log.log;

import com.yuzhou.log.annotation.CommonLog;
import com.yuzhou.log.common.LogType;
import com.yuzhou.log.common.MethodWrapper;
import com.yuzhou.log.common.OutResultInParams;
import com.yuzhou.log.common.ResultWrapper;
import com.yuzhou.log.util.ObjectUtil;
import com.yuzhou.log.util.ReflectionUtil;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 日志服务适配器类，该类会实现一个便于扩展日志的通用方法，日志实现可继承该类，简化开发
 *
 * @author xiongcheng.lxch
 */
public abstract class LogServiceAdapter implements LogService {

    protected static final String LOG_TYPE = "logType";
    protected static final String TRACE_ID = "eagleTraceId";
    protected static final String METHOD_NAME = "methodName";
    protected static final String CLAZZ_NAME = "clazzName";
    protected static final String TIME_CONSUME = "timeConsume";
    protected static final String METHOD_RESULT = "methodResult";
    protected static final String IN_DEFINE_PARAMS = "inDefineParams";
    protected static final String OUT_DEFINDE_PARAMS = "outDefineParams";
    protected static final String ERROR_CODE = "errorCode";
    protected static final String ERROR_MSG = "errorMsg";

    protected static final String SUCCESS = "Y";
    protected static final String FAIL = "N";

    protected Logger logger = InnerLog.getLogger();


    @Override
    public String logFormat() {
        return "{logType}|{eagleTraceId}|{clazzName}|{methodName}|{timeConsume}|{methodResult}|{errorCode}|{errorMsg}|{inDefineParams}|{outDefineParams}|";
    }


    @Override
    public void logInfo(MethodWrapper methodWrapper, ResultWrapper resultWrapper) {
        try {
            Method method = methodWrapper.getMethodInvocation().getMethod();
            LogType logType = logType();
            OutResultInParams outResultInParams = outResultParams(methodWrapper.getMethodInvocation(), resultWrapper);

            String printLog;
            LogBuilder logBuilder = LogBuilder.builder(logFormat());
            if (outResultInParams == null) {
                printLog = logBuilder.replaceX(TRACE_ID, methodWrapper.getTraceId())
                        .replaceX(LOG_TYPE, logType.name())
                        .replaceX(CLAZZ_NAME, method.getDeclaringClass().getName())
                        .replaceX(METHOD_NAME, method.getName())
                        .replaceX(TIME_CONSUME, String.valueOf(methodWrapper.getTimeConsume()))
                        .build();
            } else {
                printLog = logBuilder.replaceX(TRACE_ID, methodWrapper.getTraceId())
                        .replaceX(LOG_TYPE, logType.name())
                        .replaceX(CLAZZ_NAME, method.getDeclaringClass().getName())
                        .replaceX(METHOD_NAME, method.getName())
                        .replaceX(TIME_CONSUME, String.valueOf(methodWrapper.getTimeConsume()))
                        .replaceX(METHOD_RESULT, outResultInParams.getSuccess() == null ? "" : (outResultInParams.getSuccess() ? SUCCESS : FAIL))
                        .replaceX(IN_DEFINE_PARAMS, outResultInParams.getInDefineParams() == null ? "" : outResultInParams.getInDefineParams().toString())
                        .replaceX(OUT_DEFINDE_PARAMS, outResultInParams.getOutDefineParams() == null ? "" : outResultInParams.getOutDefineParams().toString())
                        .replaceX(ERROR_CODE, outResultInParams.getErrorCode())
                        .replaceX(ERROR_MSG, outResultInParams.getErrorMsg())
                        .build();
            }
            logger.info(printLog);
        } catch (Throwable e) {
            logger.error("logInfo error", e);
        }
    }

    @Override
    public CommonLog commonLog(MethodInvocation methodInvocation) {
        return AnnotationUtils.findAnnotation(methodInvocation.getMethod(), CommonLog.class);
    }

    protected boolean throwException(ResultWrapper resultWrapper) {
        if (resultWrapper == null) {
            return false;
        }
        return resultWrapper.getThrowable() != null;
    }

    protected Map<String, Object> paramResolve(CommonLog.Param param, Object obj, boolean needPrefix) {
        Map<String, Object> paramMap = new LinkedHashMap<>();

        String[] params = param.names();
        if (param.value().length != 0) {
            params = param.value();
        }

        if (BeanUtils.isSimpleValueType(obj.getClass())) {
            paramMap.put(params[0], obj);
        } else {
            for (String s : params) {
                Object fieldValue = ReflectionUtil.getField(s, obj);
                if (ObjectUtil.isEmpty(fieldValue)) {
                    continue;
                }
                StringBuilder keyBuilder = new StringBuilder();
                if (needPrefix) {
                    keyBuilder.append(obj.getClass().getSimpleName());
                    keyBuilder.append("$");
                }
                keyBuilder.append(s);
                paramMap.put(keyBuilder.toString(), fieldValue);
            }
        }
        return paramMap;
    }
}
