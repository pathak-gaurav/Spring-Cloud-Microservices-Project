package com.gaurav.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaurav.orderservice.entity.Orders;
import com.gaurav.orderservice.entity.OrderStatus;
import com.gaurav.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private OrderProducerService orderProducerService;

    public OrderService(OrderRepository orderRepository, OrderProducerService orderProducerService) {
        this.orderRepository = orderRepository;
        this.orderProducerService = orderProducerService;
    }

    @Transactional
    public void placeOrder(Long productId, Long userId, int quantity, BigDecimal pricePerUnit)  {
        Orders ordersToPlace = Orders.builder()
                .orderStatus(OrderStatus.PENDING)
                .productId(productId)
                .quantity(quantity)
                .userId(userId)
                .totalPrice(pricePerUnit.multiply(BigDecimal.valueOf(quantity)))
                .build();

        Orders ordersPersisted = orderRepository.save(ordersToPlace);

        if (ordersPersisted != null) {
            orderProducerService.publishOrderProducer(ordersPersisted);
        }
    }
}
