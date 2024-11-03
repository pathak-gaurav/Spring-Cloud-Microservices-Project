package com.gaurav.orderservice;

import com.gaurav.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestParam Long productId, @RequestParam Long userId,
                                         @RequestParam int quantity, @RequestParam BigDecimal pricePerUnit) {
        orderService.placeOrder(productId, userId, quantity, pricePerUnit);

        return new ResponseEntity<>("Order Placed", HttpStatus.CREATED);
    }
}
