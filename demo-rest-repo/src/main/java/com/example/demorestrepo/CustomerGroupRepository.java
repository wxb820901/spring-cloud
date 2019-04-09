package com.example.demorestrepo;



import com.example.demorestrepo.entity.CustomerGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CustomerGroupRepository extends CrudRepository<CustomerGroup, Long> {
    Optional<CustomerGroup> findByGroupName(@Param("name")String groupName);
}
