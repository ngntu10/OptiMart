package com.Optimart.responses.Auth;

import com.Optimart.models.City;
import com.Optimart.models.Role;
import com.Optimart.models.ShippingAddress;
import com.Optimart.models.userShippingAddress;
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
    private City city;
    private String email;
    private String fullName;
    private String userName;
    private String firstName;
    private String middleName;
    private String address;
    private String lastName;
    private String imageUrl;
    private String phoneNumber;
    private List<userShippingAddress> addresses;
}
