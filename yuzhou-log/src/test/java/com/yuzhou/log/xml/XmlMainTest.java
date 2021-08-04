package com.yuzhou.log.xml;

import com.yuzhou.log.service.MyService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlMainTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");

        MyService myService = applicationContext.getBean(MyService.class);

        myService.sayHsf("xml test");
    }
}
