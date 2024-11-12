package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.PaymentType.PaymentTypeDTO;
import com.Optimart.dto.PaymentType.PaymentTypeMutiDeleteDTO;
import com.Optimart.dto.PaymentType.PaymentTypeSearchDTO;
import com.Optimart.models.Paymenttype;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.responses.User.UserResponse;
import com.Optimart.services.PaymentType.PaymentTypeService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Payment Type", description = "Everything about payment type")
@RequestMapping(Endpoint.PaymentType.BASE)
public class PaymentTypeController {
    private final PaymentTypeService paymentTypeService;

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list payment type")
    @GetMapping
    public ResponseEntity<PagingResponse<List<Paymenttype>>> getPaymentType(@ModelAttribute PaymentTypeSearchDTO paymentTypeSearchDTO){
        return ResponseEntity.ok(paymentTypeService.findAll(paymentTypeSearchDTO));
    }

    @PreAuthorize("hasAuthority('SETTING.PAYMENT_TYPE.CREATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = Paymenttype.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Create a new payment type")
    @PostMapping
    public ResponseEntity<APIResponse<Paymenttype>> createPaymentType(@RequestBody PaymentTypeDTO paymentTypeDTO){
        return ResponseEntity.ok(paymentTypeService.createType(paymentTypeDTO));
    }


    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Paymenttype.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get payment type details by id")
    @GetMapping(Endpoint.PaymentType.ID)
    public ResponseEntity<Paymenttype> getDetailsPaymentType(@PathVariable String paymentTypeId){
        return ResponseEntity.ok(paymentTypeService.getPaymentType(paymentTypeId));
    }

    @PreAuthorize("hasAuthority('SETTING.PAYMENT_TYPE.UPDATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Paymenttype.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update payment type by id")
    @PutMapping(Endpoint.PaymentType.ID)
    public ResponseEntity<APIResponse<Paymenttype>> editPaymentType(@RequestBody PaymentTypeDTO paymentTypeDTO, @PathVariable String paymentTypeId){
        return ResponseEntity.ok(paymentTypeService.editPaymentType(paymentTypeDTO, paymentTypeId));
    }

    @PreAuthorize("hasAuthority('SETTING.PAYMENT_TYPE.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = String.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete a payment type by id")
    @DeleteMapping(Endpoint.PaymentType.ID)
    public ResponseEntity<?> deletePaymentType(@PathVariable String paymentTypeId){
        return ResponseEntity.ok(paymentTypeService.deletePaymentType(paymentTypeId));
    }

    @PreAuthorize("hasAuthority('SETTING.PAYMENT_TYPE.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = String.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete multiple payment type by id")
    @DeleteMapping(Endpoint.PaymentType.DELETE_MANY)
    public ResponseEntity<?> deleteMutilPaymentType(@RequestBody PaymentTypeMutiDeleteDTO paymentTypeMutiDeleteDTO){
        return ResponseEntity.ok(paymentTypeService.deleteMutilPaymentType(paymentTypeMutiDeleteDTO));
    }
}
