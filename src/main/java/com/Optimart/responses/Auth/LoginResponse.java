package com.Optimart.responses.Auth;

import com.Optimart.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String message;

    private Data data;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Data {
        @JsonProperty("access_token")
        private String accessToken = "";

        @JsonProperty("refresh_token")
        private String refreshToken = "";
        private UserLoginResponse user;
    }

    public static LoginResponse success(String accessToken, String refreshToken, UserLoginResponse user) {
        Data data = new Data(accessToken, refreshToken, user);
        return new LoginResponse("Login successfully", data);
    }

    public static LoginResponse failure() {
        return new LoginResponse("Login Failed", null);
    }
}