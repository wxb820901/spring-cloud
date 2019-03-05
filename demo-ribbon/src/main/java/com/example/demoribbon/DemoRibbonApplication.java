package com.example.demoribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@RibbonClients
@RestController
@EnableCircuitBreaker
public class DemoRibbonApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRibbonApplication.class, args);
	}

	@HystrixCommand(fallbackMethod = "reliable")
	@GetMapping("/configMsg" )
	public String configMsg(){
		return customersClient.getConfigMsg();//r1 or r2
	}
	public String reliable() {
		return "Cloud Native Java (O'Reilly)";
	}
}
