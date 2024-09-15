package com.Optimart.dto.User;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditUserDTO {
     public String email;
     private int status;
     private String role;
     private String firstName;
     private String middleName;
     private String lastName;
     private String phoneNumber;
     private String address;
     private String city;
}
