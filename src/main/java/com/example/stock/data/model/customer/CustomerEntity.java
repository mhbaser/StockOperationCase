package com.example.stock.data.model.customer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Data
@Entity
@EnableJpaRepositories
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerNum;
    private String name;
    private String surname;
    private String userType;
    private String status;
    private String iban;
}
