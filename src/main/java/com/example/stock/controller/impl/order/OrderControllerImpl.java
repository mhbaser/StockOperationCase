package com.example.stock.controller.impl.order;

import com.example.stock.controller.OrderController;
import com.example.stock.controller.mapper.OrderMapper;
import com.example.stock.controller.model.Order;
import com.example.stock.data.OrderRepository;
import com.example.stock.service.CustomerService;
import com.example.stock.service.OrderService;
import com.example.stock.service.model.order.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
public class OrderControllerImpl implements OrderController {

    OrderService orderService;
    CustomerService customerService;
    Logger LOGGER = LoggerFactory.getLogger(OrderControllerImpl.class);

    @Autowired
    OrderControllerImpl(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<Order> createOrder(Order order) {
        try {
            OrderDTO orderDTO = orderService.createOrder(OrderMapper.INSTANCE.toOrderDTO(order));
            order = OrderMapper.INSTANCE.toOrder(orderDTO);
            return ResponseEntity.ok(order);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

    }

    @Override
    public ResponseEntity<List<Order>> listOrders(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        LOGGER.info("Received request with customerId: {}, startDate: {}, endDate: {}", customerId, startDate, endDate);

        List<OrderDTO> orderDTOList = orderService.getOrdersByCustomerIdAndDateRange(customerId, startDate, endDate);
        List<Order> orderList = new ArrayList<>();
        for(OrderDTO orderDTO : orderDTOList){
            Order order = OrderMapper.INSTANCE.toOrder(orderDTO);
            orderList.add(order);
        }

        if (orderList.isEmpty()) {
            LOGGER.info("No orders found for customerId: {} between {} and {}", customerId, startDate, endDate);
        }

        return ResponseEntity.ok(orderList);
    }

    @Override
    public Boolean deleteOrder(Long orderId) {

        try{
            orderService.deleteOrder(orderId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
