package com.yuzhou.log.service;


import com.yuzhou.log.annotation.CommonLog;
import com.yuzhou.log.common.LogType;
import com.yuzhou.log.javaconfig.ServiceResult;
import com.yuzhou.log.javaconfig.TestDTO;
import com.yuzhou.log.javaconfig.TestVO;

public interface MyService {
    String sayHi(String name);

    Object sayHsf(String name);

    Object sayMetaq();

    @CommonLog(type = LogType.HSF_PROVIDER)
    ServiceResult<TestVO> complex(String name, Integer age, TestDTO testDTO);

    @CommonLog(type = LogType.HSF_PROVIDER)
    ServiceResult<TestVO> throwExp(String name);
}
