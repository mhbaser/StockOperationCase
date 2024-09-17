package com.example.stock.controller.impl.customer;

import com.example.stock.controller.CustomerController;
import com.example.stock.controller.mapper.CustomerMapper;
import com.example.stock.controller.model.Customer;
import com.example.stock.service.CustomerService;
import com.example.stock.service.model.customer.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class CustomerControllerImpl implements CustomerController {

    Logger LOGGER = Logger.getLogger(CustomerControllerImpl.class.getName());
    CustomerService customerService;

    @Autowired
    public CustomerControllerImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        CustomerDTO customerDTO = CustomerMapper.INSTANCE.toCustomerDTO(customer);

        try {
           customerDTO = customerService.createCustomer(customerDTO);
            return CustomerMapper.INSTANCE.toCustomer(customerDTO);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Customer getCustomerById(Long id) {

        try {

            return CustomerMapper.INSTANCE.toCustomer(customerService.findCustomerById(id));

        } catch (EmptyResultDataAccessException e) {
            LOGGER.severe(e.getMessage());
            e.printStackTrace();
            return new Customer();
        }
    }

    @Override
    public Customer updateCustomer(Customer customer) {

        CustomerDTO customerDTO =CustomerMapper.INSTANCE.toCustomerDTO(customer);

        try {
            return CustomerMapper.INSTANCE.toCustomer(customerService.updateCustomer(customerDTO));
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Boolean deleteCustomer(Long id) {

        try {
            customerService.deleteCustomer(id);
            return true;
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            e.printStackTrace();
            return false;
        }

    }
}
