package com.Optimart.services.ProductType;

import com.Optimart.constants.Endpoint;
import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.ProductType.ProductTypeDTO;
import com.Optimart.dto.ProductType.ProductTypeMutiDeleteDTO;
import com.Optimart.dto.ProductType.ProductTypeSearchDTO;
import com.Optimart.models.Product;
import com.Optimart.models.ProductType;
import com.Optimart.repositories.ProductTypeRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.utils.LocalizationUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductTypeService implements IProductTypeService {
    private final ProductTypeRepository productTypeRepository;
    private final ModelMapper modelMapper;
    private final LocalizationUtils localizationUtils;

    @Override
    public PagingResponse<List<ProductType>> getAllProductType(ProductTypeSearchDTO productTypeSearchDTO) {
        Pageable pageable = PageRequest.of(productTypeSearchDTO.getPage() - 1, productTypeSearchDTO.getLimit(), Sort.by("createdAt").descending());
        if (StringUtils.hasText(productTypeSearchDTO.getOrder())) {
            String order = productTypeSearchDTO.getOrder();
            String[] orderParams = order.split("-");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(productTypeSearchDTO.getPage() - 1, productTypeSearchDTO.getLimit(), Sort.by(new Sort.Order(direction, orderParams[0])));
            }
        }
        Page<ProductType> productTypes = StringUtils.hasText(productTypeSearchDTO.getSearch()) ? productTypeRepository.findByNameContainingIgnoreCase(productTypeSearchDTO.getSearch(), pageable) : productTypeRepository.findAll(pageable);
        return new PagingResponse<>(productTypes.getContent(), localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_TYPE_GET_SUCCESS), productTypes.getTotalPages(), productTypes.getTotalElements());
    }

    @Override
    public APIResponse<ProductType> createType(ProductTypeDTO productTypeDTO) {
        ProductType productType = modelMapper.map(productTypeDTO, ProductType.class);
        productTypeRepository.save(productType);
        return new APIResponse<>(productType, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_TYPE_CREATE_SUCCESS));
    }

    @Override
    public ProductType getProductType(String id) {
        return productTypeRepository.findById(UUID.fromString(id)).get();
    }

    @Override
    public APIResponse<ProductType> editProductType(ProductTypeDTO productTypeDTO, String productTypeId) {
        ProductType productType = productTypeRepository.findById(UUID.fromString(productTypeId)).get();
        modelMapper.map(productTypeDTO, productType);
        productTypeRepository.save(productType);
        return new APIResponse<>(productType, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_TYPE_UPDATE_SUCCESS));
    }

    @Override
    public APIResponse<Boolean> deleteProductType(String productTypeId) {
        productTypeRepository.deleteById(UUID.fromString(productTypeId));
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_TYPE_DELETE_SUCCESS));
    }

    @Override
    public APIResponse<Boolean> deleteMultiProductType(ProductTypeMutiDeleteDTO productTypeMutiDeleteDTO) {
        List<String> productTypeIds = productTypeMutiDeleteDTO.getProductTypeIds();
        productTypeIds.forEach(item -> {
            productTypeRepository.deleteById(UUID.fromString(item));
        });
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_TYPE_DELETE_SUCCESS));
    }
}
