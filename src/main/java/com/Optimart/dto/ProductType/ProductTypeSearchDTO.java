package com.Optimart.dto.ProductType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeSearchDTO {
    private int limit = 10;
    private int page = 1;
    private String search;
    private String order;
}