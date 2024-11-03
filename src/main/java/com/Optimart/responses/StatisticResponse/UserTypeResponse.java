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
public class UserTypeResponse {
    @JsonProperty("1")
    private Long googleUser;

    @JsonProperty("2")
    private Long facebookUser;

    @JsonProperty("3")
    private Long emailUser;
}
