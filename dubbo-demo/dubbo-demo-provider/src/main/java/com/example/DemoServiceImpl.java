package com.example;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;


@Service(interfaceClass = DemoService.class)
@Component
public class DemoServiceImpl implements DemoService {
    public String sayHello(String name) {
        System.out.println("hello " + name + "!");
        return "hello " + name + "!";
    }
}
