package com.example;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@EnableDubboConfiguration
@SpringBootApplication
public class DemoCustomerApplication {
    @Autowired
    private HelloConsumer helloConsumer;

    public static void main(String[] args) {
        SpringApplication.run(DemoCustomerApplication.class, args);
    }
    @PostConstruct
    public void init() {
        helloConsumer.getDemoService().sayHello("bill");
    }
}
