package com.Optimart.responses;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse {
    private Date timestamp;

    private String message;
}
