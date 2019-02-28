package com.example.demorestrepo;


import com.example.demorestrepo.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Scanner;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class DemoRestRepoApplication {

	public static void main(String[] args) {
//		System.out.println("which profiles do you want? r1 or r2");
//		Scanner scan = new Scanner(System.in);
//		String profiles = scan.nextLine();
//		String profiles = System.getenv("ACTIVE_PROFILES");
//		new SpringApplicationBuilder(DemoRestRepoApplication.class).profiles(profiles).run(args);
		SpringApplication.run(DemoRestRepoApplication.class, args);
	}
	@Autowired
	DataInitializer initializer;

	@Value("${test.message}")
	private String text;

	@PostConstruct
	public void init() {

		Customer.CustomerId customerId = initializer.initializeCustomer();
//		initializer.initializeOrder(customerId);
	}

	@GetMapping("/configMsg" )
	public String configMsg(){
		return text;
	}

}

