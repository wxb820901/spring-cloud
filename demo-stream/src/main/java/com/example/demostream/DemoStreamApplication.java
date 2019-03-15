package com.example.demostream;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableEurekaClient
@EnableBinding({Distination.class})
public class DemoStreamApplication {
    Logger logger = LoggerFactory.getLogger(DemoStreamApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(DemoStreamApplication.class, args);
    }


    @PostMapping("/send/{msg}")
    public String send(@PathVariable("msg") String msg){
        source.output().send(MessageBuilder.withPayload(msg).build());
        return "SUCCESS";
    }

    @StreamListener(Distination.INPUT)
    public void recieve(Object payload){
        logger.info("received from kafka =======>"+payload);
    }

    @Autowired
    private Distination source;
}