package com.example;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Reference(url = "dubbo://${dubbo.provider.hostname}:12345")
    private DemoService demoService;

    @RequestMapping("/sayHello")
    public String sayHello() {
        return demoService.sayHello("bill");
    }
}
