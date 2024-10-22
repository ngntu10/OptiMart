package com.Optimart.services.Payment;

import com.Optimart.responses.Payment.VNPAYResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface IPaymentService {
    VNPAYResponse createVnPayPayment(Map<String, Object> requestData, HttpServletRequest request);
}
