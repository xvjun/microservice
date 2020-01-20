package com.xujun.user;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UserEdgeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserEdgeServiceApplication.class,args);
    }
}
