package com.example.stock.service.model.customer;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {

    private Long id;
    private Long customerNum;
    private String name;
    private String surname;
    private String userType;
    private String status;
    private String iban;

}
