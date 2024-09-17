package com.example.stock.service;

import com.example.stock.service.model.order.OrderDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    OrderDTO createOrder(OrderDTO orderDTO);
    List<OrderDTO> getOrdersByCustomerIdAndDateRange(Long customerId, LocalDateTime startDate, LocalDateTime endDate);
    Boolean deleteOrder(Long orderId);

}
