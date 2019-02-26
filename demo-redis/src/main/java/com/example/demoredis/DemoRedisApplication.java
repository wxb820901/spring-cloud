package com.example.demoredis;

import com.example.demorestrepo.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DemoRedisApplication {

	@Autowired
	PersonService ps = new PersonService();

	public static void main(String[] args) {
		SpringApplication.run(DemoRedisApplication.class, args);
	}

	@PostConstruct
	public void init() {
		ps.save("n1");
		ps.save("n2");
		ps.save("n3");
		System.out.println("====>"+ps.getPersons());

	}
}
