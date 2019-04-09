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
public class CustomerGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;

    public CustomerGroup(String groupName) {
        this.groupName = groupName;
    }


    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Customer> customers = new HashSet();
}
