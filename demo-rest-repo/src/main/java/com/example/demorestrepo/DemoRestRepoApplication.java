package com.example.demorestrepo;


import com.example.demorestrepo.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableEurekaClient
public class DemoRestRepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRestRepoApplication.class, args);
	}

	@Autowired
	DataInitializer initializer;

	@PostConstruct
	public void init() {

		Customer.CustomerId customerId = initializer.initializeCustomer();
//		initializer.initializeOrder(customerId);
	}

}

