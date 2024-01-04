package com.yuzhou.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
public class App {

    final static int loop = 10000;

    final static ExecutorService executorService = Executors.newFixedThreadPool(20);

    final static AtomicInteger counter = new AtomicInteger(1);

    final static CountDownLatch latch = new CountDownLatch(loop);

    public static void main(String[] args) throws Exception{
        try {
            for (int i = 0; i < loop; i++) {
                executorService.execute(() -> {
                    try {
                        Thread.sleep(10);
                        JSONObject jsonObject = get();
                        Integer code = jsonObject.getInteger("code");
                        if(503 == code){
                            System.err.println("==============网关错误....."+ jsonObject.getString("msg"));
                        }else if(code != 200){
                            System.err.println(".>>>>>>>>>>>>>>>" + jsonObject.getString("msg")+"-----------"+jsonObject.getString("errorMsg"));
                        }

                        if(200 == code){
                            counter.getAndIncrement();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        latch.countDown();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        latch.await();
        System.out.println("=<<<<<<<end<<<<<<< ....success : "+counter.get());
    }

    public static JSONObject get() throws Exception {
        String s = Request.Get("http://localhost:8999/hua5-service-member/app/follow/getFollowRelation?otherUserIds=1584819888283369473")
                .addHeader("hua5-auth", "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRfaWQiOiIxMDAiLCJ1c2VyX25hbWUiOiJhZG1pbiIsIm1vYmlsZSI6IjE1OTI2NDk1NzAyIiwicmVhbF9uYW1lIjoiYWRtaW4iLCJhdmF0YXIiOiJodHRwOi8vaHVhNS1ncm91cC5vc3MtY24taGFuZ3pob3UuYWxpeXVuY3MuY29tL3VwbG9hZC8yMDIzMDIyOC84ZmRiZWM4MWE2OWRiMTRhYzZkOGY3Zjg4MGQ2ZDgzOS5qcGciLCJhdXRob3JpdGllcyI6WyJhcHAtcmVnaXN0ZXIiLCJhZG1pbiJdLCJjbGllbnRfaWQiOiJodWE1LWFkbWluIiwicm9sZV9uYW1lIjoiYXBwLXJlZ2lzdGVyLGFkbWluIiwibGljZW5zZSI6InBvd2VyZWQgYnkgaHVhNXgiLCJwb3N0X2lkIjoiMTEyMzU5ODgxNzczODY3NTIwMSIsInVzZXJfaWQiOiIxMTIzNTk4ODIxNzM4Njc1MjAxIiwicm9sZV9pZCI6IjE1OTM1NTI1NzcxMDU5MDc3MTQsMTU5Mjc0OTQyNzkyNzI0MDcwNiIsInNjb3BlIjpbImFsbCJdLCJuaWNrX25hbWUiOiLotoXnuqfnrqHnkIblkZgiLCJvYXV0aF9pZCI6IiIsImRldGFpbCI6eyJ0eXBlIjoid2ViIn0sImV4cCI6MTY4ODEyMzQ3OCwiZGVwdF9pZCI6IjExMjM1OTg4MTM3Mzg2NzUyMDEiLCJqdGkiOiI3YmI3N2U0OTZlMjM5M2U2ZTFhNzI1ZjY0YmYxOWIzZCIsImFjY291bnQiOiJhZG1pbiJ9.xzHnFT-HdXrG98JL45KKwxVWi8aS9DqRaQDow3nRKdc")
                .addHeader("authorization", "Basic aHVhNS1hZG1pbjpkYTFmZDczZmYzNTk3MDA4ZGVkYmRkMmQzZDE0NjRhM2UwYTBkZjQwOjE6MTY4ODAzMTI5Njg1NTo4MGEyNjI0NzU3MWQ2YjgzNWQxM2ZiYzRmMTNiYmU0NDp1ZTlqRERjUmFxTm5EOHE0N09sRGpVbmx6ZklCYzFNMDoyY2FiZTY0ZTlkNmU1MTRkMjJmYzliN2JhMWQ0ODFmYg==")
                .addHeader("Accept", "application/json, text/plain, */*")
                .execute()
                .returnContent().asString(Charset.defaultCharset());
        return JSON.parseObject(s);
    }
}
