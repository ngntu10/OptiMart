package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.annotations.UnsecuredSwaggerOperation;
import com.Optimart.configuration.Redis.RedisConfig;
import com.Optimart.constants.Endpoint;
import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Product.*;
import com.Optimart.models.Product;
import com.Optimart.responses.CloudinaryResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.responses.Product.ProductResponse;
import com.Optimart.services.Product.ProductService;
import com.Optimart.services.Redis.Product.ProductRedisService;
import com.Optimart.utils.LocalizationUtils;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Tag(name = "Product", description = "Everything about product")
@RequestMapping(Endpoint.Product.BASE)
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final LocalizationUtils localizationUtils;
    private final ProductService productService;
    private final ProductRedisService productRedisService;

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.PRODUCT.VIEW') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list products")
    @GetMapping
    public ResponseEntity<?> getListProducts(@RequestParam Map<Object, String> filters){
        try{
            List<ProductResponse> productResponses = productRedisService
                    .getAllProducts(filters);
            if (productResponses!=null && !productResponses.isEmpty()) {
                PagingResponse<List<ProductResponse>> listPagingResponse = new PagingResponse<>(productResponses,
                        localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_SUCCESS),
                        1, (long) productResponses.size());
                logger.info(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_SUCCESS)  + " redis");
                return ResponseEntity.ok(listPagingResponse);
            }
            PagingResponse<List<ProductResponse>> pagingResponse = productService.findAllProduct(filters);
            productRedisService.saveAllProducts(pagingResponse.getData(), filters);
            return ResponseEntity.ok(pagingResponse);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.PRODUCT.CREATE') OR hasAuthority('ADMIN.GRANTED')")
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

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.PRODUCT.UPDATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update an existing product")
    @PutMapping(Endpoint.Product.ID)
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @RequestBody ProductDTO product){
        return ResponseEntity.ok(productService.updateProduct(product, productId));
    }

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.PRODUCT.UPDATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CloudinaryResponse.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update Product image")
    @PostMapping(Endpoint.Product.CHANGE_IMAGE)
    public ResponseEntity<CloudinaryResponse> updateAvatar(@RequestParam String productId,
                                                           @RequestParam("file") MultipartFile file) {
        CloudinaryResponse response = productService.uploadImage(productId, file);
        return ResponseEntity.ok(new CloudinaryResponse(response.getPublicId(), response.getUrl(), localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_IMAGE_UPDATE_SUCCESS)));
    }

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.PRODUCT.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete a product by id")
    @DeleteMapping(Endpoint.Product.ID)
    public ResponseEntity<?> deleteProduct(@PathVariable String productId){
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list public product (Status = 1)")
    @GetMapping(Endpoint.Product.PUBLIC)
    public ResponseEntity<?> getListProductsPublic(@RequestParam Map<Object, String> filters){
        return ResponseEntity.ok(productService.findAllProductPublic(filters));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list related product")
    @GetMapping(Endpoint.Product.RELATED)
    public ResponseEntity<?> getListProductsRelate(@RequestParam Map<Object, String> filters){
        return ResponseEntity.ok(productService.getListProductRelatedTo(filters));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get a public product by id (Status = 1)")
    @GetMapping(Endpoint.Product.PUBLIC_ID)
    public ResponseEntity<?> getPublicProductById(@PathVariable String productId, @RequestBody Boolean isViewed){
        return ResponseEntity.ok(productService.getOneProductPublic(productId, isViewed));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @UnsecuredSwaggerOperation(summary = "Get list public slug by id (Status = 1)")
    @GetMapping(Endpoint.Product.PUBLIC_SLUG_ID)
    public ResponseEntity<?> getPublicSlugProductById(@PathVariable String slugId,
                                                      @RequestParam Map<String, String> params){
        return ResponseEntity.ok(productService.getOneProductBySlug(slugId, params));
    }

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.PRODUCT.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete multi product")
    @DeleteMapping(Endpoint.Product.DELETE_MANY)
    public ResponseEntity<?> deleteMultiProduct(@RequestBody ProductMultiDeleteDTO productMultiDeleteDTO){
        return ResponseEntity.ok(productService.deleteMultiProduct(productMultiDeleteDTO));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "User like a product")
    @PostMapping(Endpoint.Product.LIKE)
    public ResponseEntity<?> likeProduct(@RequestBody ReactionProductDTO reactionProductDTO, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(productService.likeProduct(reactionProductDTO, token));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "User unlike a product")
    @PostMapping(Endpoint.Product.UNLIKE)
    public ResponseEntity<?> unlikeProduct(@RequestBody ReactionProductDTO reactionProductDTO, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(productService.unlikeProduct(reactionProductDTO, token));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list product liked")
    @GetMapping(Endpoint.Product.LIKED_ME)
    public ResponseEntity<?> getProductLiked(@RequestParam Map<Object, String> filters,  @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(productService.getLikedProducts(filters, token));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list product viewed")
    @GetMapping(Endpoint.Product.VIEWED_ME)
    public ResponseEntity<?> getProductViewed(@RequestParam Map<Object, String> filters,  @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(productService.getViewedProducts(filters, token));
    }
}
