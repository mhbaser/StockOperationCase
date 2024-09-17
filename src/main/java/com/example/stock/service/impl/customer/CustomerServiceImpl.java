package com.example.stock.service.impl.customer;

import com.example.stock.data.CustomerRepository;
import com.example.stock.data.model.customer.CustomerEntity;
import com.example.stock.service.CustomerService;
import com.example.stock.service.mapper.CustomerServiceMapper;
import com.example.stock.service.model.customer.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {this.customerRepository = customerRepository;}

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        try{
            CustomerEntity customerEntity = CustomerServiceMapper.INSTANCE.toCustomerEntity(customerDTO);
            return CustomerServiceMapper.INSTANCE.toCustomerDTO(customerRepository.save(customerEntity));

        }catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public CustomerDTO findCustomerById(Long id) {
        CustomerEntity customerEntity;
            customerEntity= customerRepository.findById(id).orElse(null);
            return CustomerServiceMapper.INSTANCE.toCustomerDTO(customerEntity);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {


            CustomerEntity customerEntity= customerRepository.save(CustomerServiceMapper.INSTANCE.toCustomerEntity(customerDTO));
            return CustomerServiceMapper.INSTANCE.toCustomerDTO(customerEntity);



    }

    @Override
    public Boolean deleteCustomer(Long id) {

            customerRepository.deleteById(id);
            return true;
    }
}
