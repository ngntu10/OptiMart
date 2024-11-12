package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.deliveryType.DeliveryTypeDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeMutilDeleteDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeSearchDTO;
import com.Optimart.models.DeliveryType;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.services.DeliveryType.DeliveryTypeService;
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
@Tag(name = "Delivery Type", description = "Everything about delivery type")
@RequestMapping(Endpoint.DeliveryType.BASE)
public class DeliveryTypeController {
    private final DeliveryTypeService deliveryTypeService;

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeliveryType.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list delivery type")
    @GetMapping
    public ResponseEntity<PagingResponse<List<DeliveryType>>> getDeliveryType(@ModelAttribute DeliveryTypeSearchDTO deliveryTypeSearchDTO){
        return ResponseEntity.ok(deliveryTypeService.findAll(deliveryTypeSearchDTO));
    }

    @PreAuthorize("hasAuthority('SETTING.DELIVERY_TYPE.CREATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = DeliveryType.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Create a new delivery type")
    @PostMapping
    public ResponseEntity<APIResponse<DeliveryType>> createDeliveryType(@RequestBody DeliveryTypeDTO deliveryTypeDTO){
        return ResponseEntity.ok(deliveryTypeService.createType(deliveryTypeDTO));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DeliveryType.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get delivery type details by id")
    @GetMapping(Endpoint.DeliveryType.ID)
    public ResponseEntity<DeliveryType> getDetailsDeliveryType(@PathVariable String deliveryTypeId){
        return ResponseEntity.ok(deliveryTypeService.getDeliveryType(deliveryTypeId));
    }

    @PreAuthorize("hasAuthority('SETTING.DELIVERY_TYPE.UPDATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DeliveryType.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update delivery type by id")
    @PutMapping(Endpoint.DeliveryType.ID)
    public ResponseEntity<APIResponse<DeliveryType>> editDeliveryType(@RequestBody DeliveryTypeDTO deliveryTypeDTO, @PathVariable String deliveryTypeId){
        return ResponseEntity.ok(deliveryTypeService.editDeliveryType(deliveryTypeDTO, deliveryTypeId));
    }

    @PreAuthorize("hasAuthority('SETTING.DELIVERY_TYPE.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = String.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete a delivery type by id")
    @DeleteMapping(Endpoint.DeliveryType.ID)
    public ResponseEntity<APIResponse<Boolean>> deleteDeliveryType(@PathVariable String deliveryTypeId){
        return ResponseEntity.ok(deliveryTypeService.deleteDeliveryType(deliveryTypeId));
    }

    @PreAuthorize("hasAuthority('SETTING.DELIVERY_TYPE.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = String.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete multiple delivery type by id")
    @DeleteMapping(Endpoint.DeliveryType.DELETE_MANY)
    public ResponseEntity<APIResponse<Boolean>> deleteMutiDeliveryType(@RequestBody DeliveryTypeMutilDeleteDTO deliveryTypeMutilDeleteDTO){
        return ResponseEntity.ok(deliveryTypeService.deleteMutilDeliveryType(deliveryTypeMutilDeleteDTO));
    }
}
