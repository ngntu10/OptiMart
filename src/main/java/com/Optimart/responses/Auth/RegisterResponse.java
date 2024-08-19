package com.Optimart.responses.Auth;

import com.Optimart.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;

    public static RegisterResponse success(User user){
        return new RegisterResponse("Register successfully", user);
    }

    public static RegisterResponse failure(String message){
        return new RegisterResponse(message, null);
    }
}
