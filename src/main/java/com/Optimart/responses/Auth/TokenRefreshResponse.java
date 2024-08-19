package com.Optimart.responses.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRefreshResponse {
    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("refreshToken")
    private String refreshToken;
//    private String tokenType = "Bearer";

    @JsonProperty("message")
    private String message;

    public static TokenRefreshResponse success(String accessToken, String refreshToken, String message) {
        return new TokenRefreshResponse(accessToken, refreshToken, message);
    }

    public static TokenRefreshResponse failure(String message) {
        return new TokenRefreshResponse("","", message);
    }
}
