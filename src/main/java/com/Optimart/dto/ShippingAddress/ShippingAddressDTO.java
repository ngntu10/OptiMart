package com.Optimart.dto.ShippingAddress;

import com.Optimart.models.City;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingAddressDTO {
    private String id;
    private String address;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private Boolean isDefault;
    @JsonIgnore
    private City city;

    private String cityId;

    @JsonCreator
    public ShippingAddressDTO(
            @JsonProperty("city") Object cityObject,
            @JsonProperty("address") String address,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("middleName") String middleName,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("isDefault") Boolean isDefault) {
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phoneNumber = phoneNumber;
        this.isDefault = isDefault;
        if (cityObject instanceof String) {
            this.cityId = (String) cityObject;
        } else if (cityObject instanceof Map<?,?>) {
            Object id = ((Map<?, ?>) cityObject).get("id");
            if (id != null) {
                this.cityId = id.toString();
            }
        }
    }
}
