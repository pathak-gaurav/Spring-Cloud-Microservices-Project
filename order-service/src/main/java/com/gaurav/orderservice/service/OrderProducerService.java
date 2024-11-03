package com.gaurav.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaurav.orderservice.entity.Orders;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class OrderProducerService {

    private KafkaTemplate<String, String> kafkaTemplate;
    private String orderProducerTopic;
    private ObjectMapper objectMapper;
    private ExecutorService executorService;

    public OrderProducerService(KafkaTemplate kafkaTemplate, @Value("${kafka.topic.name}") String orderProducerTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper = new ObjectMapper();
        this.executorService = Executors.newFixedThreadPool(5);
        this.orderProducerTopic = orderProducerTopic;
    }

    public CompletableFuture<String> publishOrderProducer(Orders ordersPersisted) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String orderInJson = objectMapper.writeValueAsString(ordersPersisted);
                kafkaTemplate.send(orderProducerTopic, orderInJson)
                        .get(10, TimeUnit.SECONDS);

                return "Order successfully published to Kafka";

            } catch (JsonProcessingException e) {
                throw new CompletionException("Failed to process order", e);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new CompletionException("Failed to send order to Kafka", e);
            }
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
