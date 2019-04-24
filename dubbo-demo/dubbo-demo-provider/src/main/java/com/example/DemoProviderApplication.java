package com.example;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@EnableDubboConfiguration
@SpringBootApplication
public class DemoProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoProviderApplication.class, args);
    }
}
