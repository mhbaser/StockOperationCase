package com.example.stock.service.impl.order;

import com.example.stock.data.AssetRepository;
import com.example.stock.data.OrderRepository;
import com.example.stock.data.model.asset.AssetEntity;
import com.example.stock.data.model.constants.OrderSide;
import com.example.stock.data.model.constants.OrderStatus;
import com.example.stock.data.model.order.OrderEntity;
import com.example.stock.service.AssetService;
import com.example.stock.service.CustomerService;
import com.example.stock.service.mapper.OrderServiceMapper;
import com.example.stock.service.model.asset.AssetConstants;
import com.example.stock.service.model.customer.CustomerDTO;
import com.example.stock.service.model.order.OrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetService assetService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_BuyOrder_Success() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerId(1L);
        orderEntity.setOrderSide(OrderSide.BUY);
        orderEntity.setPrice(BigDecimal.valueOf(100));
        orderEntity.setOrderSize(BigDecimal.valueOf(1));
        orderEntity.setAssetName("BTC");

        AssetEntity tryAsset = new AssetEntity();
        tryAsset.setUsableSize(BigDecimal.valueOf(1000));

        when(customerService.findCustomerById(1L)).thenReturn(customerDTO);
        when(assetRepository.findByCustomerIdAndAssetName(1L, AssetConstants.TRY.name())).thenReturn(Optional.of(tryAsset));
        when(assetRepository.findByCustomerIdAndAssetName(1L, "BTC")).thenReturn(Optional.empty());

        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderDTO result = orderServiceImpl.createOrder(OrderServiceMapper.INSTANCE.toDTO(orderEntity));

        assertNotNull(result);
        verify(assetRepository, times(2)).save(any(AssetEntity.class));
    }

    @Test
    void testGetOrdersByCustomerIdAndDateRange_Success() {
        // Arrange: Create some mock OrderEntity objects
        List<OrderEntity> orderEntityList = new ArrayList<>();
        OrderEntity orderEntity1 = new OrderEntity();
        orderEntity1.setCustomerId(1L);
        orderEntity1.setOrderSize(new BigDecimal(2));
        orderEntity1.setPrice(new BigDecimal(100));
        orderEntityList.add(orderEntity1);

        OrderEntity orderEntity2 = new OrderEntity();
        orderEntity2.setCustomerId(1L);
        orderEntity2.setOrderSize(new BigDecimal(3));
        orderEntity2.setPrice(new BigDecimal(300));
        orderEntityList.add(orderEntity2);

        // Mock the repository method call
        when(orderRepository.findOrdersByCustomerIdAndDateRange(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(orderEntityList);

        // Act: Call the method with mocked repository
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        List<OrderDTO> result = orderServiceImpl.getOrdersByCustomerIdAndDateRange(1L, startDate, endDate);

        // Assert: Ensure the size of the result matches
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getCustomerId());
        assertEquals(new BigDecimal(2), result.get(0).getOrderSize());
        assertEquals(new BigDecimal(100), result.get(0).getPrice());

        assertEquals(1L, result.get(1).getCustomerId());
        assertEquals(new BigDecimal(3), result.get(1).getOrderSize());
        assertEquals(new BigDecimal(300), result.get(1).getPrice());
    }

    @Test
    void testCreateOrder_SellOrder_InsufficientBalance() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomerId(1L);
        orderDTO.setOrderSide(OrderSide.SELL);
        orderDTO.setPrice(BigDecimal.valueOf(100));
        orderDTO.setOrderSize(BigDecimal.valueOf(2));
        orderDTO.setAssetName("BTC");

        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setUsableSize(BigDecimal.valueOf(100));

        when(customerService.findCustomerById(1L)).thenReturn(customerDTO);
        when(assetRepository.findByCustomerIdAndAssetName(1L, "BTC")).thenReturn(Optional.of(assetEntity));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderServiceImpl.createOrder(orderDTO);
        });

        assertEquals("Customer should deposit a TRY to perform operation.", exception.getMessage());
    }

    @Test
    void testDeleteOrder_Success(){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderStatus(OrderStatus.PENDING);
        orderEntity.setCustomerId(1L);
        orderEntity.setPrice(BigDecimal.valueOf(100));
        orderEntity.setOrderSize(BigDecimal.valueOf(2));

        AssetEntity tryAsset = new AssetEntity();
        tryAsset.setUsableSize(BigDecimal.valueOf(500));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        when(assetRepository.findByCustomerIdAndAssetName(1L, AssetConstants.TRY.name())).thenReturn(Optional.of(tryAsset));

        Boolean result = orderServiceImpl.deleteOrder(1L);

        assertTrue(result);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(assetRepository, times(1)).save(any(AssetEntity.class));
    }

    @Test
    void testDeleteOrder_FailedOrderStatus() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderStatus(OrderStatus.MATCHED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderServiceImpl.deleteOrder(1L);
        });

        assertEquals("You can only delete PENDING your order status is MATCHED", exception.getMessage());
    }

    @Test
    void testCheckCustomerExists_Success() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);

        when(customerService.findCustomerById(1L)).thenReturn(customerDTO);

        CustomerDTO result = orderServiceImpl.checkCustomerExists(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerService, times(1)).findCustomerById(1L);
    }

    @Test
    void testCheckCustomerExists_NotFound() {
        when(customerService.findCustomerById(1L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderServiceImpl.checkCustomerExists(1L);
        });

        assertEquals("Customer not found, first generate a customer to create order", exception.getMessage());
    }
}
