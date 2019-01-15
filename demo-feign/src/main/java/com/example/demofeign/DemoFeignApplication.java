package com.example.demofeign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class DemoFeignApplication {

	@Autowired
	CustomersClient customersClient;

	public static void main(String[] args) {
		SpringApplication.run(DemoFeignApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.out.println("====>"+customersClient.getCustomers());
		System.out.println("====>"+customersClient.getCustomer(1l));

	}

}

