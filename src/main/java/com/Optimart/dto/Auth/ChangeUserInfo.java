package com.Optimart.dto.Auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeUserInfo {
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private String address;
    private String phoneNumber;
}
