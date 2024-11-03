package com.gaurav.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaurav.orderservice.dto.OrderConfirmation;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumerService {

    private OrderService orderService;
    private ObjectMapper objectMapper;

    public OrderConsumerService(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "order-consumer", groupId = "order-service-id")
    public void orderConsumerPostStockConfirmation(OrderConfirmation orderConfirmation) {
        orderService.updateOrder(orderConfirmation);
    }
}
