package com.example.demorestrepo;


import java.util.Optional;

import com.example.demorestrepo.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findByLastname(@Param("name")String lastname);
}
