package com.example.demorestrepo;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.example.demorestrepo.entity.Customer;
import com.example.demorestrepo.entity.CustomerGroup;
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

import java.util.ArrayList;
import java.util.List;
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
		loadSentinelRule();
		SpringApplication.run(DemoRestRepoApplication.class, args);
	}
	@Autowired
	DataInitializer initializer;

	@Value("${test.message}")
	private String text;

	@PostConstruct
	public void init() {
		Customer customer = initializer.initializeCustomer();
		System.out.println("init customer ===> "+customer);
//		initializer.initializeOrder(customerId);
	}

	@SentinelResource("config-slow-resource")
	@GetMapping("/configMsg" )
	public String configMsg(){
		return text;
	}

	private static void loadSentinelRule(){
		List<FlowRule> rules = new ArrayList<FlowRule>();
		FlowRule rule = new FlowRule();
		rule.setResource("config-slow-resource");
		// set limit qps to 10
		rule.setCount(1);
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		rule.setLimitApp("default");
		rules.add(rule);
		FlowRuleManager.loadRules(rules);
	}
}

