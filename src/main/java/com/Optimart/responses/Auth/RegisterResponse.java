package com.Optimart.responses.Auth;

import com.Optimart.models.User;
import com.Optimart.utils.LocalizationUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RegisterResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;

}
