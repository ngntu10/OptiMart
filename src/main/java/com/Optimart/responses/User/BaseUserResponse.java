package com.Optimart.responses.User;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseUserResponse {
    private UUID id;
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String imageUrl;
}
