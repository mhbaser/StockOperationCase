package com.example.stock.service.mapper;

import com.example.stock.data.model.order.OrderEntity;
import com.example.stock.service.model.order.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderServiceMapper {

    OrderServiceMapper INSTANCE = Mappers.getMapper(OrderServiceMapper.class);

    OrderEntity toEntity(OrderDTO dto);
    OrderDTO toDTO(OrderEntity entity);

}
