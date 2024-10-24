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
    private String message;
    private User user;
}
