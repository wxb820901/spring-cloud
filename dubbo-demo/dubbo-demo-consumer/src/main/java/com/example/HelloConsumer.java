package com.example;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

@Component
public class HelloConsumer {
    @Reference(url = "dubbo://127.0.0.1:20880")
    private DemoService demoService;

    public DemoService getDemoService(){
        return this.demoService;
    }
}
