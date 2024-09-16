package com.Optimart.controllers;

import com.Optimart.constants.Endpoint;
import com.Optimart.dto.deliveryType.DeliveryTypeDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeMutilDeleteDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeSearchDTO;
import com.Optimart.repositories.DeliveryTypeRepository;
import com.Optimart.services.DeliveryType.DeliveryTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Delivery Type", description = "Everything about delivery type")
@RequestMapping(Endpoint.DeliveryType.BASE)
public class DeliveryTypeController {
    private final DeliveryTypeService deliveryTypeService;
    @GetMapping
    public ResponseEntity<?> getDeliveryType(@RequestBody DeliveryTypeSearchDTO deliveryTypeSearchDTO){
        return ResponseEntity.ok(deliveryTypeService.findAll(deliveryTypeSearchDTO));
    }

    @PostMapping
    public ResponseEntity<?> createDeliveryType(@RequestBody DeliveryTypeDTO deliveryTypeDTO){
        return ResponseEntity.ok(deliveryTypeService.createType(deliveryTypeDTO));
    }

    @GetMapping(Endpoint.DeliveryType.ID)
    public ResponseEntity<?> getDetailsDeliveryType(@PathVariable String deliveryTypeId){
        return ResponseEntity.ok(deliveryTypeService.getDeliveryType(deliveryTypeId));
    }

    @PatchMapping(Endpoint.DeliveryType.ID)
    public ResponseEntity<?> editDeliveryType(@RequestBody DeliveryTypeDTO deliveryTypeDTO){
        return ResponseEntity.ok(deliveryTypeService.editDeliveryType(deliveryTypeDTO));
    }

    @DeleteMapping(Endpoint.DeliveryType.ID)
    public ResponseEntity<?> deleteDeliveryType(@PathVariable String deliveryTypeId){
        return ResponseEntity.ok(deliveryTypeService.deleteDeliveryType(deliveryTypeId));
    }

    @DeleteMapping(Endpoint.DeliveryType.DELETE_MANY)
    public ResponseEntity<?> deleteMutiDeliveryType(@RequestBody DeliveryTypeMutilDeleteDTO deliveryTypeMutilDeleteDTO){
        return ResponseEntity.ok(deliveryTypeService.deleteMutilDeliveryType(deliveryTypeMutilDeleteDTO));
    }
}
