package com.Optimart.controllers;

import com.Optimart.constants.Endpoint;
import com.Optimart.dto.ProductType.ProductTypeDTO;
import com.Optimart.dto.ProductType.ProductTypeMutiDeleteDTO;
import com.Optimart.dto.ProductType.ProductTypeSearchDTO;
import com.Optimart.services.ProductType.ProductTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Product Type", description = "Everything about product type")
@RequestMapping(Endpoint.ProductType.BASE)
public class ProductTypeController {
    private final ProductTypeService productTypeService;
    @GetMapping
    public ResponseEntity<?> getListProductType(@ModelAttribute ProductTypeSearchDTO productTypeSearchDTO){
         return ResponseEntity.ok(productTypeService.getAllProductType(productTypeSearchDTO));
    }

    @PostMapping
    public ResponseEntity<?> createNewProductType(@RequestBody ProductTypeDTO productTypeDTO){
        return ResponseEntity.ok(productTypeService.createType(productTypeDTO));
    }

    @GetMapping(Endpoint.ProductType.ID)
    public ResponseEntity<?> getOneProductType(@PathVariable String productTypeId){
        return ResponseEntity.ok(productTypeService.getProductType(productTypeId));
    }

    @DeleteMapping(Endpoint.ProductType.ID)
    public ResponseEntity<?> deleteProductType(@PathVariable String productTypeId){
        return ResponseEntity.ok(productTypeService.deleteProductType(productTypeId));
    }

    @PutMapping(Endpoint.ProductType.ID)
    public ResponseEntity<?> updateProductType(@PathVariable String productTypeId, @RequestBody ProductTypeDTO productTypeDTO){
        return ResponseEntity.ok(productTypeService.editProductType(productTypeDTO, productTypeId));
    }

    @DeleteMapping(Endpoint.ProductType.DELETE_MANY)
    public ResponseEntity<?> deleteMultiProductType(@RequestBody ProductTypeMutiDeleteDTO productTypeMutiDeleteDTO){
        return ResponseEntity.ok(productTypeService.deleteMultiProductType(productTypeMutiDeleteDTO));
    }
}
