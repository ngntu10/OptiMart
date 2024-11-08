package com.Optimart.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ViewProduct {
    private String isPublic;
    private String isViewed;
}
