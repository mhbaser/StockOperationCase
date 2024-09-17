package com.example.stock.service.mapper;

import com.example.stock.data.model.customer.CustomerEntity;
import com.example.stock.service.model.customer.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerServiceMapper {

    CustomerServiceMapper INSTANCE = Mappers.getMapper(CustomerServiceMapper.class);

    CustomerEntity toCustomerEntity(CustomerDTO customerDTO);
    CustomerDTO toCustomerDTO(CustomerEntity customerEntity);


}
