package com.example.demorestrepo;

import com.example.demorestrepo.entity.Customer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class DataInitializer {

//    private final @NonNull OrderRepository orders;
    @Autowired
    private @NonNull CustomerRepository customers;


    @Transactional("customerTransactionManager")
    public Customer.CustomerId initializeCustomer() {
        return customers.save(new Customer("Dave", "Matthews")).getId();
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