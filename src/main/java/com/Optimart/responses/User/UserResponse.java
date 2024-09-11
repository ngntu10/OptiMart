package com.Optimart.responses.User;

import com.Optimart.models.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Role role;
    private String phoneNumber;
    private String cityName;
    private int status;
    private int userType;
}
