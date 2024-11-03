package com.gaurav.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gaurav.orderservice.entity.OrderStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderConfirmation {

    private Long orderId;
    @JsonProperty("status")
    private OrderStatus status;
}
