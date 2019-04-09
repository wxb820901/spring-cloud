package com.example.demorestrepo.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(of = "id")
@Data
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname, lastname;


    public Customer(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Customer(String firstname, String lastname, CustomerGroup cg) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.CustomerGroups.add(cg);
    }

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    private Set<CustomerGroup> CustomerGroups = new HashSet();
}