package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.constants.MessageKeys;
import com.Optimart.models.DeliveryType;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Payment.VNPAYResponse;
import com.Optimart.services.Order.OrderService;
import com.Optimart.services.Payment.PaymentService;
import com.Optimart.utils.LocalizationUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(Endpoint.Payment.BASE)
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Everything about payment by VNPAY")
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final LocalizationUtils localizationUtils;

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeliveryType.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Create VNPAY payment url")
    @PostMapping(Endpoint.Payment.VNPAY)
    public ResponseEntity<?> pay(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        return ResponseEntity.ok(paymentService.createVnPayPayment(requestData, request));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeliveryType.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get status payment vnpay")
    @GetMapping(Endpoint.Payment.CALL_BACK)
    public ResponseEntity<?> payCallbackHandler(HttpServletRequest request) throws FirebaseMessagingException {
        String status = request.getParameter("vnp_ResponseCode");
        String orderId = request.getParameter("vnp_OrderInfo");
        long amount = Long.parseLong(request.getParameter("vnp_Amount"))/100;
        if (status.equals("00")) {
            orderService.handlePaymentOrderById(orderId);
            APIResponse<VNPAYResponse> response = new APIResponse<>(new VNPAYResponse(status, localizationUtils.getLocalizedMessage(MessageKeys.PAY_SUCCESS), "", amount), "Success");
            return ResponseEntity.ok(response);
        } else {
            APIResponse<VNPAYResponse> response = new APIResponse<>(null, localizationUtils.getLocalizedMessage(MessageKeys.PAY_FAILED));
            return ResponseEntity.badRequest().body(response);
        }
    }
}
