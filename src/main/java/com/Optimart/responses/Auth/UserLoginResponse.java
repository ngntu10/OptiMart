package com.Optimart.responses.Auth;

import com.Optimart.models.Role;
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
    private Role role;
    private String email;
    private String fullName;
    private String userName;
    private String firstName;
    private String middleName;
    private String address;
    private String lastName;
    private String imageUrl;
    private String phoneNumber;
}
