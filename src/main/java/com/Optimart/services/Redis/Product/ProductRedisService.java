package com.Optimart.services.Redis.Product;

import com.Optimart.responses.Product.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductRedisService implements IProductRedisService{
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;
//    @Value("${spring.data.redis.use-redis-cache}")
//    private boolean useRedisCache;

    private String getKeyFrom(Map<Object, String> filters) {
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        String order = filters.getOrDefault("order", "asc");
        String productType = filters.getOrDefault("productType", "default_type");
        String status = filters.getOrDefault("status", "default_status");
        String search = filters.getOrDefault("search", "default_search");
        String productLocation = filters.getOrDefault("productLocation", "default_location");
        int minStar = Integer.parseInt(filters.getOrDefault("minStar", "0"));

        return String.format("all_products:%s:%s:%d:%d:%s:%s:%s:%d",
                search, productType, page, limit, order, status, productLocation, minStar);
    }

    @Override
    public List<ProductResponse> getAllProducts(Map<Object, String> filters) throws JsonProcessingException {

//        if(useRedisCache == false) {
//            return null;
//        }

        String key = this.getKeyFrom(filters);
        String json = (String) redisTemplate.opsForValue().get(key);
        List<ProductResponse> productResponses =
                json != null ?
                        redisObjectMapper.readValue(json, new TypeReference<List<ProductResponse>>() {})
                        : null;
        return productResponses;
    }
    @Override
    public void clear(){
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Override
    //save to Redis
    public void saveAllProducts(List<ProductResponse> productResponses, Map<Object, String> filters) throws JsonProcessingException {
        String key = this.getKeyFrom(filters);
        String json = redisObjectMapper.writeValueAsString(productResponses);
        redisTemplate.opsForValue().set(key, json);
    }
}

