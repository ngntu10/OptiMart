package com.Optimart.dto.ShippingAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingAddressDTO {
    private String fullName;
    private String address;
    private String city;
    private String phoneNumber;
}
