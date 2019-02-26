package com.example.demoredis;


import com.example.demorestrepo.entity.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRep extends CrudRepository<Person, String> {

}
