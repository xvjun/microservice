package com.xujun.course;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableDubbo
@SpringBootApplication
public class CourseDubboServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseDubboServiceApplication.class,args);
    }
}
