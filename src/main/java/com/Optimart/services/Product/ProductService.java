package com.Optimart.services.Product;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Product.CreateProductDTO;
import com.Optimart.dto.Product.ProductDTO;
import com.Optimart.dto.Product.ProductMultiDeleteDTO;
import com.Optimart.models.Product;
import com.Optimart.repositories.ProductRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final LocalizationUtils localizationUtils;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    public APIResponse<Product> createProduct(CreateProductDTO createProductDTO) {
        Product product = modelMapper.map(createProductDTO, Product.class);
        productRepository.save(product);
        return new APIResponse<>(product, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_CREATE_SUCCESS));
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
