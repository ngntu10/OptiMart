package com.Optimart.dto.Review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WriteReviewDTO {
    @JsonProperty("product")
    private String productId;
    @JsonProperty("user")
    private String userId;
    private String content;
    private double star;
}
