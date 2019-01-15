package com.example.demorestrepo;


import java.util.Optional;

import com.example.demorestrepo.entity.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data repository to manage {@link Customer}s.
 *
 * @author Oliver Gierke
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByLastname(String lastname);
}
