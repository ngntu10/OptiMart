package com.Optimart.controllers;

import com.Optimart.constants.Endpoint;
import com.Optimart.dto.Product.CreateProductDTO;
import com.Optimart.dto.Product.ProductDTO;
import com.Optimart.dto.Product.ProductMultiDeleteDTO;
import com.Optimart.dto.Product.ProductSearchDTO;
import io.swagger.v3.oas.annotations.Parameter;
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

    @GetMapping
    public ResponseEntity<?> getListProducts(@Parameter Map<String, String> filters){

        return null;
    }

    @PostMapping
    public ResponseEntity<?> createNewProduct(@RequestBody CreateProductDTO createProductDTO){

        return null;
    }

    @GetMapping(Endpoint.Product.ID)
    public ResponseEntity<?> getOneProduct(@PathVariable String productId){

        return null;
    }

    @PostMapping(Endpoint.Product.ID)
    public ResponseEntity<?> updateProduct(@PathVariable String productId){

        return null;
    }

    @DeleteMapping(Endpoint.Product.ID)
    public ResponseEntity<?> deleteProduct(@PathVariable String productId){

        return null;
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

        return null;
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
