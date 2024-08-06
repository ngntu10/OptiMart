package com.Optimart.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    @JsonProperty("status")
    private int status;

    @JsonProperty("timestamp")
    private Date timestamp;

    @JsonProperty("message")
    private String message;

    @JsonProperty("details")
    private String details;
}
