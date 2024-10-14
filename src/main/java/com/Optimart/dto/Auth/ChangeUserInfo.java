package com.Optimart.dto.Auth;

import com.Optimart.dto.ShippingAddress.ShippingAddressDTO;
import lombok.*;

import java.util.List;

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
    private List<ShippingAddressDTO> addresses;
}
