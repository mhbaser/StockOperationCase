package com.example.stock.data;

import com.example.stock.data.model.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    OrderEntity save (OrderEntity orderEntity);

    @Query("SELECT o FROM OrderEntity o " +
            "WHERE o.customerId = :customerId " +
            "AND o.orderDate BETWEEN :startDate AND :endDate")
    List<OrderEntity> findOrdersByCustomerIdAndDateRange(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


}
