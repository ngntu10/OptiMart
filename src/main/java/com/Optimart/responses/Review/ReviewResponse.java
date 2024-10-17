package com.Optimart.responses.Review;

import com.Optimart.responses.Product.BaseProductResponse;
import com.Optimart.responses.User.BaseUserResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private UUID id;
    private String content;
    private double star;
    private BaseProductResponse product;
    private BaseUserResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
