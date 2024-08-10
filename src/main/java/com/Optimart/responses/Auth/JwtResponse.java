package com.Optimart.responses.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    @JsonProperty("token")
    private String token;

    @JsonProperty("refreshToken")
    private String refreshToken;
}
