package com.example.stock.controller.impl.order;

import com.example.stock.controller.model.Order;
import com.example.stock.service.CustomerService;
import com.example.stock.service.OrderService;
import com.example.stock.service.model.order.OrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class OrderControllerImplTest {

    @Mock
    private OrderService orderService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private OrderControllerImpl orderControllerImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_Success() {
        Order order = new Order();
        order.setAssetName("BTC");

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAssetName("BTC");

        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(orderDTO);

        ResponseEntity<Order> response = orderControllerImpl.createOrder(order);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("BTC", response.getBody().getAssetName());
        verify(orderService, times(1)).createOrder(any(OrderDTO.class));
    }

    @Test
    void testCreateOrder_Exception() {
        Order order = new Order();
        when(orderService.createOrder(any(OrderDTO.class))).thenThrow(new RuntimeException("Error creating order"));

        ResponseEntity<Order> response = orderControllerImpl.createOrder(order);

        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(orderService, times(1)).createOrder(any(OrderDTO.class));
    }

    @Test
    void testListOrders_Success() {
        OrderDTO orderFirst = new OrderDTO();
        orderFirst.setAssetName("BTC");
        orderFirst.setOrderSize(new BigDecimal(1.5));

        OrderDTO orderSecond = new OrderDTO();
        orderSecond.setAssetName("ETH");
        orderSecond.setOrderSize(new BigDecimal(3.0));

        List<OrderDTO> orderDTOList = Arrays.asList(orderFirst,orderSecond);

        when(orderService.getOrdersByCustomerIdAndDateRange(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(orderDTOList);

        ResponseEntity<List<Order>> response = orderControllerImpl.listOrders(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("BTC", response.getBody().get(0).getAssetName());
        assertEquals("ETH", response.getBody().get(1).getAssetName());
        verify(orderService, times(1)).getOrdersByCustomerIdAndDateRange(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testListOrders_Empty() {
        when(orderService.getOrdersByCustomerIdAndDateRange(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        ResponseEntity<List<Order>> response = orderControllerImpl.listOrders(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(orderService, times(1)).getOrdersByCustomerIdAndDateRange(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
    }


    @Test
    void testDeleteOrder_Exception() {
        doThrow(new RuntimeException("Error deleting order")).when(orderService).deleteOrder(anyLong());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderControllerImpl.deleteOrder(1L);
        });

        assertEquals("java.lang.RuntimeException: Error deleting order", exception.getMessage());
        verify(orderService, times(1)).deleteOrder(anyLong());
    }
}
