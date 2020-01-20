package com.xujun.course;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableDubbo
@ServletComponentScan
public class CourseEdgeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseEdgeServiceApplication.class,args);
    }
}

