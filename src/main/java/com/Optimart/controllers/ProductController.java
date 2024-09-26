package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Product.CreateProductDTO;
import com.Optimart.dto.Product.ProductDTO;
import com.Optimart.dto.Product.ProductMultiDeleteDTO;
import com.Optimart.dto.Product.ProductSearchDTO;
import com.Optimart.models.Product;
import com.Optimart.responses.CloudinaryResponse;
import com.Optimart.responses.User.UserResponse;
import com.Optimart.services.CloudinaryService;
import com.Optimart.services.Product.ProductService;
import com.Optimart.utils.LocalizationUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@RestController
@Tag(name = "Product", description = "Everything about product")
@RequestMapping(Endpoint.Product.BASE)
public class ProductController {
    private final LocalizationUtils localizationUtils;
    private final ProductService productService;
    private final CloudinaryService cloudinaryService;
    @GetMapping
    public ResponseEntity<?> getListProducts(@RequestParam Map<Object, String> filters){
        return ResponseEntity.ok(productService.findAllProduct(filters));
    }

    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Create a new product")
    @PostMapping
    public ResponseEntity<?> createNewProduct(@RequestBody CreateProductDTO createProductDTO){
        return ResponseEntity.ok(productService.createProduct(createProductDTO));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get detail a product by id")
    @GetMapping(Endpoint.Product.ID)
    public ResponseEntity<?> getOneProduct(@PathVariable String productId){
        return ResponseEntity.ok(productService.getOneProduct(productId));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update an existing product")
    @PutMapping(Endpoint.Product.ID)
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @RequestBody ProductDTO product){
        return ResponseEntity.ok(productService.updateProduct(product, productId));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CloudinaryResponse.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update Product image")
    @PostMapping(Endpoint.Product.CHANGE_IMAGE)
    public ResponseEntity<CloudinaryResponse> updateAvatar(@RequestParam String productId,
                                                           @RequestParam("file") MultipartFile file) {
        CloudinaryResponse response = productService.uploadImage(productId, file);
        return ResponseEntity.ok(new CloudinaryResponse(response.getPublicId(), response.getUrl(), localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_IMAGE_UPDATE_SUCCESS)));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete a product by id")
    @DeleteMapping(Endpoint.Product.ID)
    public ResponseEntity<?> deleteProduct(@PathVariable String productId){
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list public product (Status = 1)")
    @GetMapping(Endpoint.Product.PUBLIC)
    public ResponseEntity<?> getListProductsPublic(@Parameter Map<Object, String> filters){
        return ResponseEntity.ok(productService.findAllProductPublic(filters));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list related product")
    @GetMapping(Endpoint.Product.RELATED)
    public ResponseEntity<?> getListProductsRelate(@Parameter Map<String, String> filters){

        return null;
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get a public product by id (Status = 1)")
    @GetMapping(Endpoint.Product.PUBLIC_ID)
    public ResponseEntity<?> getPublicProductById(@PathVariable String productId, @RequestBody Boolean isViewed){
        return ResponseEntity.ok(productService.getOneProductPublic(productId, isViewed));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list public slug by id (Status = 1)")
    @GetMapping(Endpoint.Product.PUBLIC_SLUG_ID)
    public ResponseEntity<?> getPublicSlugProductById(@PathVariable String slugId){

        return null;
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete multi product")
    @DeleteMapping(Endpoint.Product.DELETE_MANY)
    public ResponseEntity<?> deleteMultiProduct(@RequestBody ProductMultiDeleteDTO productMultiDeleteDTO){
        return ResponseEntity.ok(productService.deleteMultiProduct(productMultiDeleteDTO));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "User like a product")
    @PostMapping(Endpoint.Product.LIKE)
    public ResponseEntity<?> likeProduct(@RequestBody ProductDTO productDTO){

        return null;
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "User unlike a product")
    @PostMapping(Endpoint.Product.UNLIKE)
    public ResponseEntity<?> unlikeProduct(@RequestBody ProductDTO productDTO){

        return null;
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list product liked")
    @GetMapping(Endpoint.Product.LIKED_ME)
    public ResponseEntity<?> getProductLiked(@RequestBody ProductSearchDTO productSearchDTO){

        return null;
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list product viewed")
    @GetMapping(Endpoint.Product.VIEWED_ME)
    public ResponseEntity<?> getProductViewed(@RequestBody ProductSearchDTO productSearchDTO){

        return null;
    }
}
