package com.example.demorestrepo;

import com.example.demorestrepo.entity.Customer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * Sample component to demonstrate how to work with repositories backed by different {@link DataSource}s. Note how we
 * explicitly select a transaction manager by name. In this particular case (only one operation on the repository) this
 * is not strictly necessary. However, if multiple repositories or multiple interactions on the very same repository are
 * to be executed in a method we need to expand the transaction boundary around these interactions. It's recommended to
 * create a dedicated annotation meta-annotated with {@code @Transactional("…")} to be able to refer to a particular
 * data source without using String qualifiers.
 * <p>
 * Also, not that one cannot interact with both databases in a single, transactional method as transactions are thread
 * bound in Spring an thus only a single transaction can be active in a single thread. See {@link Application#init()}
 * for how to orchestrate the calls.
 *
 * @author Oliver Gierke
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {

//    private final @NonNull OrderRepository orders;
    @Autowired
    private @NonNull CustomerRepository customers;

    /**
     * Initializes a {@link Customer}.
     *
     * @return
     */
    @Transactional("customerTransactionManager")
    public Customer.CustomerId initializeCustomer() {
        return customers.save(new Customer("Dave", "Matthews")).getId();
    }

    /**
     * Initializes an {@link Order}.
     *
     * @param customer must not be {@literal null}.
     * @return
     */
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