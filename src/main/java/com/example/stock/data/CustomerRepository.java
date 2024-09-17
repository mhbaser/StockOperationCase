package com.example.stock.data;

import com.example.stock.data.model.customer.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository  extends JpaRepository<CustomerEntity, Long> {

    CustomerEntity save (CustomerEntity customerEntity);
    void  deleteById(long id);
}
