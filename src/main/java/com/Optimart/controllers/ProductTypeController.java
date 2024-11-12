package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.ProductType.ProductTypeDTO;
import com.Optimart.dto.ProductType.ProductTypeMutiDeleteDTO;
import com.Optimart.dto.ProductType.ProductTypeSearchDTO;
import com.Optimart.models.Product;
import com.Optimart.models.ProductType;
import com.Optimart.services.ProductType.ProductTypeService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Product Type", description = "Everything about product type")
@RequestMapping(Endpoint.ProductType.BASE)
public class ProductTypeController {
    private final ProductTypeService productTypeService;

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductType.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list product type")
    @GetMapping
    public ResponseEntity<?> getListProductType(@ModelAttribute ProductTypeSearchDTO productTypeSearchDTO){
         return ResponseEntity.ok(productTypeService.getAllProductType(productTypeSearchDTO));
    }

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.PRODUCT_TYPE.CREATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Create a new product type")
    @PostMapping
    public ResponseEntity<?> createNewProductType(@RequestBody ProductTypeDTO productTypeDTO){
        return ResponseEntity.ok(productTypeService.createType(productTypeDTO));
    }

    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = ProductType.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get a product type")
    @GetMapping(Endpoint.ProductType.ID)
    public ResponseEntity<?> getOneProductType(@PathVariable String productTypeId){
        return ResponseEntity.ok(productTypeService.getProductType(productTypeId));
    }

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.PRODUCT_TYPE.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete an existing product type")
    @DeleteMapping(Endpoint.ProductType.ID)
    public ResponseEntity<?> deleteProductType(@PathVariable String productTypeId){
        return ResponseEntity.ok(productTypeService.deleteProductType(productTypeId));
    }

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.PRODUCT_TYPE.UPDATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductType.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update an existing product type by id")
    @PutMapping(Endpoint.ProductType.ID)
    public ResponseEntity<?> updateProductType(@PathVariable String productTypeId, @RequestBody ProductTypeDTO productTypeDTO){
        return ResponseEntity.ok(productTypeService.editProductType(productTypeDTO, productTypeId));
    }

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.PRODUCT_TYPE.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete multi product type")
    @DeleteMapping(Endpoint.ProductType.DELETE_MANY)
    public ResponseEntity<?> deleteMultiProductType(@RequestBody ProductTypeMutiDeleteDTO productTypeMutiDeleteDTO){
        return ResponseEntity.ok(productTypeService.deleteMultiProductType(productTypeMutiDeleteDTO));
    }
}
