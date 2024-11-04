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
public class RevenueResponse {
    @JsonProperty("total")
    private Long total;

    @JsonProperty("month")
    private Long month;

    @JsonProperty("year")
    private Long year;
}
