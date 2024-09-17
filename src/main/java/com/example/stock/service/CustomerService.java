package com.example.stock.service;

import com.example.stock.service.model.customer.CustomerDTO;

public interface CustomerService {

    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO findCustomerById(Long id);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    Boolean deleteCustomer(Long id);

}
