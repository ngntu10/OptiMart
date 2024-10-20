package com.Optimart.responses.Payment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VNPAYResponse {
    public String code;
    public String message;
    public String paymentUrl;
}
