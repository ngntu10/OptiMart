package com.Optimart.responses.StatisticResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusStats {
    @JsonProperty("0")
    private Long waitPayment;

    @JsonProperty("1")
    private Long waitDelivery;

    @JsonProperty("2")
    private Long doneOrder;

    @JsonProperty("3")
    private Long cancelOrder;

}
