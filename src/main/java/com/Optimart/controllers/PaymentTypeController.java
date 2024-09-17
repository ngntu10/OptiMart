package com.Optimart.controllers;

import com.Optimart.constants.Endpoint;
import com.Optimart.dto.PaymentType.PaymentTypeDTO;
import com.Optimart.dto.PaymentType.PaymentTypeMutiDeleteDTO;
import com.Optimart.dto.PaymentType.PaymentTypeSearchDTO;
import com.Optimart.services.PaymentType.PaymentTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Auth", description = "Everything about auth")
@RequestMapping(Endpoint.PaymentType.BASE)
public class PaymentTypeController {
    private final PaymentTypeService paymentTypeService;
    @GetMapping
    public ResponseEntity<?> getPaymentType(@ModelAttribute PaymentTypeSearchDTO paymentTypeSearchDTO){
        return ResponseEntity.ok(paymentTypeService.findAll(paymentTypeSearchDTO));
    }

    @PostMapping
    public ResponseEntity<?> createPaymentType(@RequestBody PaymentTypeDTO paymentTypeDTO){
        return ResponseEntity.ok(paymentTypeService.createType(paymentTypeDTO));
    }

    @GetMapping(Endpoint.PaymentType.ID)
    public ResponseEntity<?> getDetailsPaymentType(@PathVariable String paymentTypeId){
        return ResponseEntity.ok(paymentTypeService.getPaymentType(paymentTypeId));
    }

    @PutMapping(Endpoint.PaymentType.ID)
    public ResponseEntity<?> editPaymentType(@RequestBody PaymentTypeDTO paymentTypeDTO, @PathVariable String paymentTypeId){
        return ResponseEntity.ok(paymentTypeService.editPaymentType(paymentTypeDTO, paymentTypeId));
    }

    @DeleteMapping(Endpoint.PaymentType.ID)
    public ResponseEntity<?> deletePaymentType(@PathVariable String paymentTypeId){
        return ResponseEntity.ok(paymentTypeService.deletePaymentType(paymentTypeId));
    }

    @DeleteMapping(Endpoint.PaymentType.DELETE_MANY)
    public ResponseEntity<?> deleteMutiPaymentType(@RequestBody PaymentTypeMutiDeleteDTO paymentTypeMutiDeleteDTO){
        return ResponseEntity.ok(paymentTypeService.deleteMutilPaymentType(paymentTypeMutiDeleteDTO));
    }
}
