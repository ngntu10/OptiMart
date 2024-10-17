package com.Optimart.dto.Review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReviewDTO {
    private String id;
    private String content;
    private double star;
}
