package com.example.stock.data.model.order;

import com.example.stock.data.model.constants.OrderSide;
import com.example.stock.data.model.constants.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@EnableJpaRepositories
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    private String assetName;
    private OrderSide orderSide;
    private BigDecimal orderSize;
    private BigDecimal price;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
}
