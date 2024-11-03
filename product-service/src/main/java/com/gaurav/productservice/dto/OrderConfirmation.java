package com.gaurav.productservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderConfirmation {

    private Long orderId;
    private Status status;
}

