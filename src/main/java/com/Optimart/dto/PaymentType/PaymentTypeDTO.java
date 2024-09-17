package com.Optimart.dto.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTypeDTO {
    private String id;
    private String name;
    private String type;
}