package com.Optimart.responses.User;

import com.Optimart.models.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String fullName;
    private String email;
    private Role role;
    private String phoneNumber;
    private String cityName;
    private int status;
    private int userType;
}
