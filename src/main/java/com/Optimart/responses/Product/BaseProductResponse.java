package com.Optimart.responses.Product;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseProductResponse {
    private UUID id;
    private String name;
    private String slug;
}
