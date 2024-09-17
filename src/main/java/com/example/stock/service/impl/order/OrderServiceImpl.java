package com.example.stock.service.impl.order;

import com.example.stock.data.AssetRepository;
import com.example.stock.data.OrderRepository;
import com.example.stock.data.model.asset.AssetEntity;
import com.example.stock.data.model.constants.OrderSide;
import com.example.stock.data.model.constants.OrderStatus;
import com.example.stock.data.model.order.OrderEntity;
import com.example.stock.service.AssetService;
import com.example.stock.service.CustomerService;
import com.example.stock.service.OrderService;
import com.example.stock.service.mapper.OrderServiceMapper;
import com.example.stock.service.model.asset.AssetConstants;
import com.example.stock.service.model.customer.CustomerDTO;
import com.example.stock.service.model.order.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    AssetRepository assetRepository;
    AssetService assetService;
    CustomerService customerService;

    @Autowired
    OrderServiceImpl(OrderRepository orderRepository,
                     CustomerService customerService,
                     AssetRepository assetRepository,AssetService assetService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.assetRepository = assetRepository;
        this.assetService = assetService;
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {

        checkCustomerExists(orderDTO.getCustomerId());
        checkInputs(orderDTO.getOrderSize());
        AssetEntity tryAssetEntity = checkTryAssetExists(orderDTO.getCustomerId());

        if(Objects.equals(orderDTO.getOrderSide().name(), OrderSide.BUY.name())){
            BigDecimal buyAmount = orderDTO.getOrderSize().multiply(orderDTO.getPrice());
            if (tryAssetEntity.getUsableSize().compareTo(buyAmount)<0) {
                throw new RuntimeException("Insufficient TRY balance for BUY order");
            }
            tryAssetEntity.setUsableSize(tryAssetEntity.getUsableSize().subtract(buyAmount));
            assetRepository.save(tryAssetEntity);
            AssetEntity buyAssetEntity = checkTradeAssetExists(orderDTO.getCustomerId(),orderDTO.getAssetName());
            if(Objects.isNull(buyAssetEntity.getId())){
                buyAssetEntity.setUsableSize(BigDecimal.ZERO.add(buyAmount));
                buyAssetEntity.setSize(BigDecimal.ZERO.add(buyAmount));
                buyAssetEntity.setAssetName(orderDTO.getAssetName());
                buyAssetEntity.setCustomerId(orderDTO.getCustomerId());
                assetRepository.save(buyAssetEntity);
            }
        }

        if(Objects.equals(orderDTO.getOrderSide().name(), OrderSide.SELL.name())){
            BigDecimal sellAmount = orderDTO.getOrderSize().multiply(orderDTO.getPrice());
           AssetEntity sellAssetEntity = checkTradeAssetExists(orderDTO.getCustomerId(),orderDTO.getAssetName());
           if(sellAssetEntity.getUsableSize().compareTo(sellAmount)<0){
               throw new RuntimeException("Insufficient Choosen Asset Balance for SELL order");
           }
           if(Objects.isNull(sellAssetEntity.getId())){
                throw new RuntimeException("You should buy an asset to sell.");
            }
           sellAssetEntity.setUsableSize(sellAssetEntity.getUsableSize().subtract(orderDTO.getOrderSize().multiply(orderDTO.getPrice())));
           tryAssetEntity.setUsableSize(tryAssetEntity.getUsableSize().add(orderDTO.getOrderSize().multiply(orderDTO.getPrice())));

           assetRepository.save(sellAssetEntity);
           assetRepository.save(tryAssetEntity);
        }

        orderDTO.setOrderStatus(OrderStatus.PENDING);
        orderDTO.setOrderDate(LocalDateTime.now());
        return OrderServiceMapper.INSTANCE.toDTO(orderRepository.save(OrderServiceMapper.INSTANCE.toEntity(orderDTO)));

    }

    public CustomerDTO checkCustomerExists(long customerId) {
        CustomerDTO customerDTO = customerService.findCustomerById(customerId);
        if (Objects.isNull(customerDTO)) {
            throw new RuntimeException("Customer not found, first generate a customer to create order");
        }
        return customerDTO;
    }

    public AssetEntity checkTradeAssetExists(long customerId,String assetName) {
        return assetRepository.findByCustomerIdAndAssetName(customerId,assetName).orElse(new AssetEntity());

    }

    public AssetEntity checkTryAssetExists(long customerId) {
        AssetEntity assetEntity = assetRepository.findByCustomerIdAndAssetName(customerId,AssetConstants.TRY.name())
                .orElseThrow(()-> new RuntimeException("Customer should deposit a TRY to perform operation."));
        return assetEntity;
    }

    public void checkInputs(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Please enter a valid positive value.");
        }
    }

    @Override
    public List<OrderDTO> getOrdersByCustomerIdAndDateRange(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        List<OrderEntity> orderEntityList = orderRepository.findOrdersByCustomerIdAndDateRange(customerId, startDate, endDate);
        List<OrderDTO> orderDTOList = new ArrayList<>();

        for(OrderEntity orderEntity : orderEntityList){
            OrderDTO orderDTO = OrderServiceMapper.INSTANCE.toDTO(orderEntity);
            orderDTOList.add(orderDTO);
        }

        return orderDTOList;
    }

    @Override
    public Boolean deleteOrder(Long orderId) {
        try{
            OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
            if(!Objects.isNull(orderEntity)) {
                if (Objects.equals(OrderStatus.PENDING, orderEntity.getOrderStatus())) {
                    AssetEntity assetEntity = assetRepository.findByCustomerIdAndAssetName(orderEntity.getCustomerId(),AssetConstants.TRY.name()).orElse(null);
                    if (!Objects.isNull(assetEntity)) {
                        assetEntity.setUsableSize(assetEntity.getUsableSize().add(orderEntity.getPrice().multiply(orderEntity.getPrice())));
                        assetRepository.save(assetEntity);
                    }
                    orderEntity.setOrderStatus(OrderStatus.CANCELLED);
                    orderRepository.save(orderEntity);
                    return true;
                } else {
                    throw new Exception("You can only delete " + OrderStatus.PENDING.name() + " your order status is " + orderEntity.getOrderStatus());
                }
            }
            else{
                 throw new Exception("There is no order with id " + orderId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
