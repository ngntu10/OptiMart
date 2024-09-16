package com.Optimart.dto.deliveryType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTypeSearchDTO {
    private int limit = 10;
    private int page = 1;
    private String search;
    private String order;
}
