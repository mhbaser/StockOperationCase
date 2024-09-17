package com.example.stock.controller.mapper;

import com.example.stock.controller.model.Order;
import com.example.stock.service.model.order.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toOrder(OrderDTO orderDTO);
    OrderDTO toOrderDTO(Order order);
}
