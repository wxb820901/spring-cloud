package com.example.demorestrepo;


import java.util.Optional;

import com.example.demorestrepo.entity.Customer;
import org.springframework.data.repository.CrudRepository;


public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByLastname(String lastname);
}
