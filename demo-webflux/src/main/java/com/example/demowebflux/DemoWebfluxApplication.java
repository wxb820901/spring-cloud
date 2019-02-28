package com.example.demowebflux;

import com.example.demorestrepo.entity.Customer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@SpringBootApplication
@RestController
@RequestMapping("/Customers")
@EnableEurekaClient
public class DemoWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoWebfluxApplication.class, args);
	}

	@GetMapping("/{id}")
	private Mono<Customer> getCustomerById(@PathVariable String id) {
		return Mono.just(new Customer("firstName","lastName"));
	}

	@GetMapping
	private Flux<Customer> getAllEmployees() {
		return Flux.fromIterable(Arrays.asList(new Customer("AkkName1","AllName1"), new Customer("AkkName2","AllName2")));
	}

}
