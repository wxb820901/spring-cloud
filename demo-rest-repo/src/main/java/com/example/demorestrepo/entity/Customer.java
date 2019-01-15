package com.example.demorestrepo.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Simple domain class representing a {@link com.example.api.entity.Customer}.
 *
 * @author Oliver Gierke
 */
@Entity
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Customer {

    private @Id @GeneratedValue Long id;
    private final String firstname, lastname;

    public Customer() {
        this.firstname = null;
        this.lastname = null;
    }

    public Customer(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public CustomerId getId() {
        return new CustomerId();
    }

    @Value
    @Embeddable
    @RequiredArgsConstructor
    @SuppressWarnings("serial")
    public static class CustomerId implements Serializable {

        private final Long customerId;

        CustomerId() {
            this.customerId = null;
        }
    }
}