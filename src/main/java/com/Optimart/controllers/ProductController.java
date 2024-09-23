package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.Product.CreateProductDTO;
import com.Optimart.dto.Product.ProductDTO;
import com.Optimart.dto.Product.ProductMultiDeleteDTO;
import com.Optimart.dto.Product.ProductSearchDTO;
import com.Optimart.models.DeliveryType;
import com.Optimart.models.Product;
import com.Optimart.services.Product.ProductService;
import com.Optimart.utils.LocalizationUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Service
@RequiredArgsConstructor
@RestController
@Tag(name = "Product", description = "Everything about product")
@RequestMapping(Endpoint.Product.BASE)
public class ProductController {
    private final LocalizationUtils localizationUtils;
    private final ProductService productService;
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

    @GetMapping(Endpoint.Product.ID)
    public ResponseEntity<?> getOneProduct(@PathVariable String productId){
        return ResponseEntity.ok(productService.getOneProduct(productId));
    }

    @PutMapping(Endpoint.Product.ID)
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @RequestBody ProductDTO product){
        return ResponseEntity.ok(productService.updateProduct(product, productId));
    }

    @DeleteMapping(Endpoint.Product.ID)
    public ResponseEntity<?> deleteProduct(@PathVariable String productId){
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }

    @GetMapping(Endpoint.Product.PUBLIC)
    public ResponseEntity<?> getListProductsPublic(@Parameter Map<String, String> filters){

        return null;
    }

    @GetMapping(Endpoint.Product.RELATED)
    public ResponseEntity<?> getListProductsRelate(@Parameter Map<String, String> filters){

        return null;
    }

    @GetMapping(Endpoint.Product.PUBLIC_ID)
    public ResponseEntity<?> getPublicProductById(@PathVariable String productId){

        return null;
    }

    @GetMapping(Endpoint.Product.PUBLIC_SLUG_ID)
    public ResponseEntity<?> getPublicSlugProductById(@PathVariable String slugId){

        return null;
    }

    @DeleteMapping(Endpoint.Product.DELETE_MANY)
    public ResponseEntity<?> deleteMultiProduct(@RequestBody ProductMultiDeleteDTO productMultiDeleteDTO){
        return ResponseEntity.ok(productService.deleteMultiProduct(productMultiDeleteDTO));
    }

    @PostMapping(Endpoint.Product.LIKE)
    public ResponseEntity<?> likeProduct(@RequestBody ProductDTO productDTO){

        return null;
    }

    @PostMapping(Endpoint.Product.UNLIKE)
    public ResponseEntity<?> unlikeProduct(@RequestBody ProductDTO productDTO){

        return null;
    }

    @GetMapping(Endpoint.Product.LIKED_ME)
    public ResponseEntity<?> getProductLiked(@RequestBody ProductSearchDTO productSearchDTO){

        return null;
    }

    @GetMapping(Endpoint.Product.VIEWED_ME)
    public ResponseEntity<?> getProductViewed(@RequestBody ProductSearchDTO productSearchDTO){

        return null;
    }
}
