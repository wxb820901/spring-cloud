package com.aws.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.context.config.annotation.EnableContextInstanceData;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableContextInstanceData
public class DemoAWSApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoAWSApplication.class, args);

    }
//    @Value("${ami-id:N/A}")
//    private String amiId;
//
//    @Value("${hostname:N/A}")
//    private String hostname;
//
//    @Value("${instance-type:N/A}")
//    private String instanceType;
//
//    @Value("${services/domain:N/A}")
//    private String serviceDomain;

    @PostConstruct
    public void init() {
//        System.out.println("init amiId ===> "+amiId);
//        System.out.println("init hostname ===> "+hostname);
//        System.out.println("init instanceType ===> "+instanceType);
//        System.out.println("init serviceDomain ===> "+serviceDomain);
    }
}
