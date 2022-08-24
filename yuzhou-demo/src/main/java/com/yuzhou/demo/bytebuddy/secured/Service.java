package com.yuzhou.demo.bytebuddy.secured;

public class Service {

    @Secured(user = "ADMIN")
    public void doAction(){
        System.out.println("运行敏感代码....");
    }
}
