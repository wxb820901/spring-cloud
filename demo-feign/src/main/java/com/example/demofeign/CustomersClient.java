package com.example.demofeign;

import com.example.demorestrepo.entity.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient("demo-rest-repo")
public interface CustomersClient {
    @RequestMapping(method = RequestMethod.GET, value = "/customers")
    Resources<Customer> getCustomers();

    @RequestMapping(method = RequestMethod.GET, value = "/customers/{customerId}", consumes = "application/json")
    Customer getCustomer(@PathVariable("customerId") Long customerId);
}
