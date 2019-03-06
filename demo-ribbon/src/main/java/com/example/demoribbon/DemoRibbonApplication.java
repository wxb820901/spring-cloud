package com.example.demoribbon;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@RestController
@EnableCircuitBreaker
public class DemoRibbonApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRibbonApplication.class, args);
	}

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
	@HystrixCommand(fallbackMethod = "reliable")
	@GetMapping("/si" )
	public ServiceInstance si(){
		ServiceInstance si = loadBalancerClient.choose("demo-rest-repo");
		return si;
	}
	@GetMapping("/configMsg" )
	public String configMsg(){
		return getRestTemplate().getForObject("http://demo-rest-repo/configMsg", String.class);
	}
	public String reliable() {
		return "Cloud Native Java (O'Reilly)";
	}
}
