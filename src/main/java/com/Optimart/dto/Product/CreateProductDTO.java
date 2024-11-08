package com.Optimart.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDTO {
    private String name;
    private String slug;
    private String type;
    private int price;
    private int discount;
    private String description;
    private String location;
    private int countInStock;
    private int status;
    private String image;
    private Date discountEndDate;
    private Date discountStartDate;
}
