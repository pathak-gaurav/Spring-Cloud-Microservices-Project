package com.gaurav.productservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderKafkaDto {

    @JsonProperty("id")
    private Long orderId;
    private Long productId;
    private Long userId;
    private int quantity;

}
