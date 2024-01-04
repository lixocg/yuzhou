package com.yuzhou;

import com.yuzhou.qiyukf.controller.AsyncTaskManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public AsyncTaskManager asyncTaskManager(){
        return new AsyncTaskManager("xxxxx");
    }
}
