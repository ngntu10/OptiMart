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
public class ModelResponse {
    @JsonProperty("user")
    private Long user;

    @JsonProperty("product")
    private Long product;

    @JsonProperty("order")
    private Long order;

    @JsonProperty("review")
    private Long review;

    @JsonProperty("revenue")
    private Long revenue;

    @JsonProperty("comment")
    private Long comment;
}
