package com.Optimart.services.Product;

import com.Optimart.dto.Product.CreateProductDTO;
import com.Optimart.dto.Product.ProductDTO;
import com.Optimart.dto.Product.ProductMultiDeleteDTO;
import com.Optimart.models.Product;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;

import java.util.List;
import java.util.Map;

public interface IProductService {
    APIResponse<Product> createProduct(CreateProductDTO createProductDTO);
    PagingResponse<List<Product>> findAllProduct(Map<String, String> filters);
    APIResponse<Product> updateProduct(ProductDTO productDTO);
    APIResponse<Boolean> deleteProduct(String productId);
    APIResponse<Boolean> deleteMultiProduct(ProductMultiDeleteDTO productMultiDeleteDTO);
    ProductDTO getOneProduct(String productId);
}
