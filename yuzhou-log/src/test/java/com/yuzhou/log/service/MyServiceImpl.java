package com.yuzhou.log.service;

import com.alibaba.fastjson.JSONObject;
import com.yuzhou.log.annotation.CommonLog;
import com.yuzhou.log.common.LogType;
import com.yuzhou.log.javaconfig.ServiceResult;
import com.yuzhou.log.javaconfig.TestDTO;
import com.yuzhou.log.javaconfig.TestVO;
import org.springframework.stereotype.Service;

@Service
public class MyServiceImpl implements MyService {

    @Override
    public String sayHi(String name) {
        System.out.println("=====target method=sayhi====");
        return "sayHi~" + name;
    }

    @Override
    @CommonLog(type = LogType.HSF_PROVIDER, inputParams = {
            @CommonLog.Param("name")
    })
    public Object sayHsf(String name) {
        System.out.println("=====target method===sayHsf==");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        return jsonObject;
    }

    @Override
    @CommonLog(type = LogType.HSF_PROVIDER)
    public Object sayMetaq() {
        System.out.println("=====target method=====");
        return new Object();
    }

    @Override
    @CommonLog(type = LogType.HSF_PROVIDER,
            inputParams = {
                    @CommonLog.Param(names = "name"),
                    @CommonLog.Param(index = 1, names = "age"),
                    @CommonLog.Param(index = 2, names = {"age", "name","tooth"})
            }
            , outputParam = @CommonLog.Param({"pAge", "pName"})
    )
    public ServiceResult<TestVO> complex(String name, Integer age, TestDTO testDTO) {
        TestVO testVO = new TestVO();
        testVO.setpName(name);
        testVO.setpSex(Boolean.TRUE);
        testVO.setpAge(age);

//        return ServiceResult.successResult(testVO);
        return ServiceResult.failedResult("errCode123","errMsg456");
    }


    @Override
    @CommonLog(type = LogType.HSF_PROVIDER)
    public ServiceResult<TestVO> throwExp(String name) {
        TestVO testVO = new TestVO();
        testVO.setpName(name);
        testVO.setpSex(Boolean.TRUE);
        testVO.setpAge(11);

        int i = 1 / 0;

//        return ServiceResult.successResult(testVO);
        return ServiceResult.failedResult("errCode123", "errMsg456");
    }

}
