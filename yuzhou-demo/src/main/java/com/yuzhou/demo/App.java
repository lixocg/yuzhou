package com.yuzhou.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.util.TypeUtils;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
public class App {

    private static AtomicInteger cnt = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        TypeUtils.compatibleWithJavaBean = true;
        String str = "{\"RequestId\":\"D4F67FDA-52AD-507E-B315-16FE4C60320B\",\"Message\":\"success\",\"TraceId\":\"0a06e1e316898581545702945d97bb\",\"Data\":[{\"Jdk\":\"Dragonwell 11\",\"GroupName\":\"_DEFAULT_GROUP\",\"GroupType\":0,\"PackageType\":\"Image\",\"ImageUrl\":\"hua5-corp-registry-vpc.cn-hangzhou.cr.aliyuncs.com/hua5x-test/hua5-adapter-service:19\",\"RunningInstances\":1,\"Replicas\":1,\"PackageVersion\":\"20230718161343\",\"GroupId\":\"131ab42b-6e48-41a1-b793-4418d39ea51f\"}],\"Code\":200,\"Success\":true}\n";
        String s = JSON.toJSONString(str);
        System.out.println(s);
    }
}
