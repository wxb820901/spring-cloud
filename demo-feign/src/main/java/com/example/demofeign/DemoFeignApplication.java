package com.example.demofeign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import feign.Retryer;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@RibbonClients
@RestController
@EnableCircuitBreaker
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
	
	@HystrixCommand(fallbackMethod = "reliable")
	@GetMapping("/configMsg" )
	public String configMsg(){
		return customersClient.getConfigMsg();//r1 or r2
	}
	public String reliable() {
	    return "Cloud Native Java (O'Reilly)";
	}
	

	  /**
	 *
	 * 注册一个重试Bean 默认FeignClient不会进行重试，使用的是{@link feign.Retryer#NEVER_RETRY}
	 *
	 * @see FeignClientsConfiguration#feignRetryer()
	 */
	
	@Bean
    public Retryer feignRetryer() {
        return new Retryer.Default();
    }
}

