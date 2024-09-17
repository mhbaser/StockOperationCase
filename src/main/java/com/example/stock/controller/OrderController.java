package com.example.stock.controller;

import com.example.stock.controller.model.Order;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
public interface OrderController {

    @PostMapping("/create")
    ResponseEntity<Order> createOrder(@RequestBody Order order);

    @GetMapping("/list")
    ResponseEntity<List<Order>> listOrders(
            @RequestParam("customerId") Long customerId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate);

    @DeleteMapping("/{orderId}")
    Boolean deleteOrder(@PathVariable("orderId") Long orderId);

}
