package com.Optimart.services.Product;

import com.Optimart.dto.Product.*;
import com.Optimart.models.Product;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.responses.Product.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface IProductService {
    APIResponse<Product> createProduct(CreateProductDTO createProductDTO);
    APIResponse<Boolean> likeProduct(ReactionProductDTO reactionProductDTO, String token);
    APIResponse<Boolean> unlikeProduct(ReactionProductDTO reactionProductDTO, String token);
    PagingResponse<List<ProductResponse>> findAllProduct(Map<Object, String> filters) throws JsonProcessingException;
    PagingResponse<List<ProductResponse>> findAllProductPublic(Map<Object, String> filters);
    PagingResponse<List<ProductResponse>> getListProductRelatedTo(Map<Object, String> filters);
    PagingResponse<List<ProductResponse>> getLikedProducts (Map<Object, String> filters, String token);
    PagingResponse<List<ProductResponse>> getViewedProducts (Map<Object, String> filters, String token);
    Product getOneProduct(String productId);
    ProductResponse getOneProductBySlug(String slug, Map<String, String> params);
    Product getOneProductPublic(String productId, Boolean isViewed);
    APIResponse<Product> updateProduct(ProductDTO product, String productId);
    APIResponse<Boolean> deleteProduct(String productId);
    APIResponse<Boolean> deleteMultiProduct(ProductMultiDeleteDTO productMultiDeleteDTO);
}
