package com.yuzhou.log.javaconfig;

import com.alibaba.fastjson.JSON;
import com.yuzhou.log.service.MyService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class MainTest {
    public static void main(String[] args) {
        try {
            AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
            MyService myService = applicationContext.getBean(MyService.class);
            myService.sayHsf("lisi");

//            myService.sayMetaq();

            TestDTO testDTO = new TestDTO();
            testDTO.setAge(142);
            testDTO.setName("djhs");
            testDTO.setTooth(Arrays.asList("1","555"));
            ServiceResult<TestVO> complex = myService.complex("张", 12, testDTO);
            System.out.println(JSON.toJSONString(complex));

//            ServiceResult<TestVO> sss = myService.throwExp("sss");

//            ExecutorService executorService = Executors.newFixedThreadPool(20);
//            for (int i = 0; i < 100; i++) {
//                final int j = i;
//                executorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        TestDTO testDTO = new TestDTO();
//                        testDTO.setAge(j);
//                        testDTO.setName("djhs====" + j);
//                        ServiceResult<TestVO> complex = myService.complex("张===" + j, j, testDTO);
//                    }
//                });
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
