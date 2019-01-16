package com.example.demorestrepo;


import com.example.demorestrepo.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class DemoRestRepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRestRepoApplication.class, args);
	}

	@Autowired
	DataInitializer initializer;

	@Autowired
	private Environment env;

	@PostConstruct
	public void init() {

		Customer.CustomerId customerId = initializer.initializeCustomer();
//		initializer.initializeOrder(customerId);
	}

	@GetMapping("/hello" )
	public String helloConfig(){
		return env.getProperty("foo.db");
	}

}

