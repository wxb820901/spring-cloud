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
		Customer customer = new Customer();
		customer.setLastname("firstName");
		customer.setLastname("lastName");
		return Mono.just(customer);
	}

	@GetMapping
	private Flux<Customer> getAllEmployees() {
		Customer customer1 = new Customer();
		customer1.setLastname("AkkName1");
		customer1.setLastname("AllName1");
		Customer customer2 = new Customer();
		customer2.setLastname("AkkName2");
		customer2.setLastname("AllName2");
		return Flux.fromIterable(Arrays.asList(customer1,customer2));
	}

}
