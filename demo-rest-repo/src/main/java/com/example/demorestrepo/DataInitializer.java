package com.example.demorestrepo;

import com.example.demorestrepo.entity.Customer;
import com.example.demorestrepo.entity.CustomerGroup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
@RequiredArgsConstructor
public class DataInitializer {

//    private final @NonNull OrderRepository orders;
    @Autowired
    private @NonNull CustomerRepository customerRepository;
    @Autowired
    private @NonNull CustomerGroupRepository customerGroupRepository;



    @Transactional("customerTransactionManager")
    public Customer initializeCustomer() {
        CustomerGroup customerGroup = new CustomerGroup();
        customerGroup.setGroupName("Gxxx");
        customerGroupRepository.save(customerGroup);
        Customer customer = new Customer();
        customer.setFirstname("Dave");
        customer.setLastname("Matthews");
        customer.setGroupId(customerGroup.getId());
        customerRepository.save(customer);
        return customer;
    }


//    @Transactional("orderTransactionManager")
//    public Order initializeOrder(CustomerId customer) {
//
//        Assert.notNull(customer, "Custoemr identifier must not be null!");
//
//        Order order = new Order(customer);
//        order.add(new LineItem("Lakewood Guitar"));
//
//        return orders.save(order);
//    }
}