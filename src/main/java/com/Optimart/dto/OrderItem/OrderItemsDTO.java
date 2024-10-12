package com.Optimart.dto.OrderItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsDTO {
    private String name;
    private int amount;
    private String image;
    private Long price;
    private int discount;
    @JsonProperty("product")
    private String productId;
    private String slug;
}
