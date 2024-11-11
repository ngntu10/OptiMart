package com.Optimart.models.Listener;

import com.Optimart.models.Product;
import com.Optimart.services.Redis.Product.IProductRedisService;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductListener {

    @Autowired
    private IProductRedisService productRedisService;
    private static final Logger logger = LoggerFactory.getLogger(ProductListener.class);

    @PrePersist
    public void prePersist(Product product) {
        logger.info("prePersist");
    }

    @PostPersist
    public void postPersist(Product product) {
        // Cập nhật Redis cache
        logger.info("postPersist");
        productRedisService.clear();
    }

    @PreUpdate
    public void preUpdate(Product product) {
        logger.info("preUpdate");
    }

    @PostUpdate
    public void postUpdate(Product product) {
        // Cập nhật Redis cache
        logger.info("postUpdate");
        productRedisService.clear();
    }

    @PreRemove
    public void preRemove(Product product) {
        logger.info("preRemove");
        productRedisService.clear();
    }

    @PostRemove
    public void postRemove(Product product) {
        // Cập nhật Redis cache
        logger.info("postRemove");
        productRedisService.clear();
    }
}
