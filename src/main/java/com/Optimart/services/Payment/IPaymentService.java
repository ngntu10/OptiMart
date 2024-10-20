package com.Optimart.services.Payment;

import com.Optimart.responses.Payment.VNPAYResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IPaymentService {
    VNPAYResponse createVnPayPayment(HttpServletRequest request);
}
