package com.example.stock.controller.model;


import com.example.stock.data.model.constants.OrderSide;
import com.example.stock.data.model.constants.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Order {

    private Long id;
    private Long customerId;
    private String assetName;
    private OrderSide orderSide;
    private BigDecimal orderSize;
    private BigDecimal price;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;

}
