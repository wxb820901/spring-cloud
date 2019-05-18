package com.example;

import org.apache.dubbo.config.annotation.Service;

@Service
public class DemoServiceImpl implements DemoService {
    public String sayHello(String name) {
        System.out.println(" say hello " + name + "!");
        return " say hello " + name + "!";
    }
}
