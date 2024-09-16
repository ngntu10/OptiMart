package com.Optimart.dto.deliveryType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTypeMutilDeleteDTO {
    List<String> deliveryTypeIds;
}
