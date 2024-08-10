package com.Optimart.responses.Auth;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginResponse {
    private UUID id;
    private String role;
    private String email;
    private String fullName;
    private String username;
    private String password;
    private String avatar;
}
