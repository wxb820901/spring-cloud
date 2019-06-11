package com.example.demomongodb;


import com.mongodb.MongoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class DemoMongodbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoMongodbApplication.class, args);
	}



	@Bean
	public MongoClient mongoClient() {
		return new MongoClient("localhost",12345);
	}

	@Bean
	public MongoDbFactory mongoDbFactory() {
		return new SimpleMongoDbFactory(new MongoClient(), "mydatabase");
	}

	@Bean
	public MongoTemplate mongoTemplate()   {
	    return new MongoTemplate(mongoDbFactory());
	}

	@Bean
	public List<Converter> mongoConverter()  {
		return  Arrays.asList(new Converter[]{new ReadConverter(), new WriteConverter()});
	}
}
