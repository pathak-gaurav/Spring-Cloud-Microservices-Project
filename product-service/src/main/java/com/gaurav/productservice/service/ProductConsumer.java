package com.gaurav.productservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaurav.productservice.dto.OrderConfirmation;
import com.gaurav.productservice.dto.OrderKafkaDto;
import com.gaurav.productservice.dto.Status;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class ProductConsumer {

    private ObjectMapper objectMapper;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ExecutorService executorService;
    private ProductService productService;
    private String orderTopic;


    public ProductConsumer(KafkaTemplate<String, String> kafkaTemplate, @Value("${kafka.order.topic}") String orderTopic, ProductService productService) {
        this.orderTopic = orderTopic;
        this.productService = productService;
        this.objectMapper = new ObjectMapper();
        this.kafkaTemplate = kafkaTemplate;
        this.executorService = Executors.newFixedThreadPool(5);
    }

    @KafkaListener(topics = "order-producer", groupId = "product-service-group")
    public void ProductOrderConsumer(String orderMessage) {
        OrderKafkaDto orderKafkaDto = null;
        try {
            orderKafkaDto = objectMapper.readValue(orderMessage, OrderKafkaDto.class);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Unable to parse the Class: " + OrderKafkaDto.class);
        }

        Long productId = orderKafkaDto.getProductId();
        boolean isStock = productService.checkStockUpdateStock(productId);

        Long orderId = orderKafkaDto.getOrderId();
        CompletableFuture.supplyAsync(() -> {
            try {
                if (isStock) {
                    OrderConfirmation orderConfirmation = OrderConfirmation.builder().orderId(orderId).status(Status.COMPLETED).build();
                    String orderConfirmationInJson = objectMapper.writeValueAsString(orderConfirmation);
                    kafkaTemplate.send(orderTopic, orderConfirmationInJson).get(10, TimeUnit.SECONDS);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new CompletionException("Failed to send order to Kafka", e);
            }catch (Exception e){
                e.printStackTrace();
            }
            return "Submitted Order to Kafka for Order Completion";
        }, executorService);
    }

    @PreDestroy
    public void cleanup() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
