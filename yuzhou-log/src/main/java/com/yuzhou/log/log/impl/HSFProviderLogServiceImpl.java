package com.yuzhou.log.log.impl;

import com.yuzhou.log.annotation.CommonLog;
import com.yuzhou.log.common.LogType;
import com.yuzhou.log.common.OutResultInParams;
import com.yuzhou.log.common.ResultWrapper;
import com.yuzhou.log.log.LogServiceAdapter;
import com.yuzhou.log.util.ReflectionUtil;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HSF provider日志打印实现
 */
public class HSFProviderLogServiceImpl extends LogServiceAdapter {

    @Override
    public LogType logType() {
        return LogType.HSF_PROVIDER;
    }


    @Override
    public OutResultInParams outResultParams(MethodInvocation methodInvocation, ResultWrapper resultWrapper) {
        OutResultInParams outResultInParams = new OutResultInParams();
        CommonLog commonLog = commonLog(methodInvocation);
        if (commonLog == null) {
            return null;
        }
        if (throwException(resultWrapper)) {
            outResultInParams.setSuccess(Boolean.FALSE);
        } else {
            Object result = resultWrapper.getResult();
            if (result != null) {
                String successField = commonLog.successField();
                Object successFiledVal = ReflectionUtil.getField(successField, result);
                //解析成功标志
                if (successFiledVal instanceof Boolean) {
                    Boolean success = (Boolean) successFiledVal;
                    outResultInParams.setSuccess(success);
                    //解析异常信息
                    if (success != null && !success) {
                        String errCode = String.valueOf(ReflectionUtil.tryField(commonLog.errCodeField(),result));
                        String errMsg = String.valueOf(ReflectionUtil.tryField(commonLog.errMessageField(),result));
                        outResultInParams.setErrorCode(errCode);
                        outResultInParams.setErrorMsg(errMsg);
                    }
                }
            }

            // 自定义输出
            Map<String, Object> outputParamMap = null;
            Object dataFieldValue = ReflectionUtil.tryField(commonLog.retDataField(),resultWrapper.getResult());

            if (dataFieldValue != null) {
                if (!(dataFieldValue instanceof Collection)) {
                    CommonLog.Param outputParam = commonLog.outputParam();
                    outputParamMap = paramResolve(outputParam, dataFieldValue, false);
                }
                if (outputParamMap != null && !outputParamMap.isEmpty()) {
                    outResultInParams.setOutDefineParams(outputParamMap);
                }
            }
        }

        //解析自定义入参
        Map<String, Object> inputParamMap = new LinkedHashMap<>();
        Object[] arguments = methodInvocation.getArguments();
        CommonLog.Param[] inputParams = commonLog.inputParams();
        if (inputParams.length > 0) {
            for (int i = 0; i < inputParams.length; i++) {
                CommonLog.Param inputParam = inputParams[i];
                if (inputParam.names().length == 0 && inputParam.value().length == 0) {
                    continue;
                }
                if(inputParam.index() > arguments.length){
                    continue;
                }
                Object argument = arguments[inputParam.index()];
                inputParamMap.putAll(paramResolve(inputParam, argument, false));
            }
        }
        if (inputParamMap != null && !inputParamMap.isEmpty()) {
            outResultInParams.setInDefineParams(inputParamMap);
        }
        return outResultInParams;
    }


}
