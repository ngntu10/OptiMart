package com.Optimart.services.Product;

import com.Optimart.dto.Product.CreateProductDTO;
import com.Optimart.dto.Product.ProductDTO;
import com.Optimart.dto.Product.ProductMultiDeleteDTO;
import com.Optimart.models.Product;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;

import java.util.List;
import java.util.Map;

public class ProductService implements IProductService {
    @Override
    public APIResponse<Product> createProduct(CreateProductDTO createProductDTO) {
        return null;
    }

    @Override
    public PagingResponse<List<Product>> findAllProduct(Map<String, String> filters) {
        return null;
    }

    @Override
    public APIResponse<Product> updateProduct(ProductDTO productDTO) {
        return null;
    }

    @Override
    public APIResponse<Boolean> deleteProduct(String productId) {
        return null;
    }

    @Override
    public APIResponse<Boolean> deleteMultiProduct(ProductMultiDeleteDTO productMultiDeleteDTO) {
        return null;
    }

    @Override
    public ProductDTO getOneProduct(String productId) {
        return null;
    }
}
