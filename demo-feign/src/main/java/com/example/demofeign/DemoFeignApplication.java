package com.example.demofeign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@RestController
public class DemoFeignApplication {

	@Autowired
	CustomersClient customersClient;
	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(DemoFeignApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.out.println("====>"+customersClient.getCustomers());
		System.out.println("====>"+customersClient.getCustomer(1l));
		System.out.println("====>"+customersClient.getConfigMsg());
	}
	
	@GetMapping("/configMsg" )
	public String configMsg(){
		return customersClient.getConfigMsg();//r1 or r2
	}

}

