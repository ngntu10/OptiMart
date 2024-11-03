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
public class ProductTypeResponse {
    @JsonProperty("total")
    private Long total;

    @JsonProperty("typeName")
    private String typeName;
}
