package com.Optimart.services.Redis.Product;

import com.Optimart.responses.Product.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface IProductRedisService {
    void clear();   //clear cache

    List<ProductResponse> getAllProducts(Map<Object, String> filters) throws JsonProcessingException;

    void saveAllProducts(List<ProductResponse> productResponses, Map<Object, String> filters) throws JsonProcessingException;
}
