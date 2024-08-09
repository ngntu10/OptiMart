package com.Optimart.responses;

import com.Optimart.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private int status;

    private String message;

    private String typeError;

    private Data data;

    private String statusMessage;

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
        private User user;
    }

}
