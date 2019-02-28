package com.example.demoredis;

import com.example.demorestrepo.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class DemoRedisApplication {

    @Autowired
    PersonService ps = new PersonService();

    public static void main(String[] args) {
        SpringApplication.run(DemoRedisApplication.class, args);
    }



    @GetMapping("/persons" )
    public List<Person> getPersons(){
        return ps.getPersons();
    }

    @PostMapping("/person/{name}" )
    public void getPersons(@PathVariable String name){
        ps.save(name);
    }
}
