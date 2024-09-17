package com.example.stock.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Customer {
    private Long id;
    private Long customerNum;
    private String name;
    private String surname;
    private String userType;
    private String status;
    private String iban;
}
