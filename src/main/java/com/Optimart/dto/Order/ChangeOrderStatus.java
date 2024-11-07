package com.Optimart.dto.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeOrderStatus {
    private String id;
    private int isDelivered;
    private int isPaid;
    private int status;
}
