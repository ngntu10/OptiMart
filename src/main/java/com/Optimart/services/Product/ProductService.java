package com.Optimart.services.Product;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Product.CreateProductDTO;
import com.Optimart.dto.Product.ProductDTO;
import com.Optimart.dto.Product.ProductMultiDeleteDTO;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.Product;
import com.Optimart.models.ProductType;
import com.Optimart.repositories.ProductRepository;
import com.Optimart.repositories.ProductTypeRepository;
import com.Optimart.repositories.Specification.ProductSpecification;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.CloudinaryResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.services.CloudinaryService;
import com.Optimart.utils.FileUploadUtil;
import com.Optimart.utils.LocalizationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final LocalizationUtils localizationUtils;
    private final CloudinaryService cloudinaryService;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ModelMapper modelMapper;
    @Override
    public APIResponse<Product> createProduct(CreateProductDTO createProductDTO) {
        Product product = modelMapper.map(createProductDTO, Product.class);
        ProductType productType = productTypeRepository.findById(UUID.fromString(createProductDTO.getType())).get();
        product.setProductType(productType);
        productRepository.save(product);
        return new APIResponse<>(product, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_CREATE_SUCCESS));
    }

    @Override
    public PagingResponse<List<Product>> findAllProduct(Map<Object, String> filters) {
        List<Product> productList;
        Pageable pageable;
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        String order = filters.get("order");
        if (page == -1 && limit == -1 ) {
            productList = productRepository.findAll();
            productList.stream()
                    .map(product -> modelMapper.map(product, Product.class))
                    .toList();
            return new PagingResponse<>(productList, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_SUCCESS), 1, (long) productList.size());
        } else {
            page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1);
            pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        }
        if (StringUtils.hasText(order)) {
            String[] orderParams = order.split("-");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(direction, orderParams[0])));
            }
        }
//        Specification<User> specification = UserSpecification.filterUsers(userSearchDTO.getRoleId(), userSearchDTO.getStatus(), userSearchDTO.getCityId(),
//                userSearchDTO.getUserType(), userSearchDTO.getSearch());

        Specification<Product> specification = null;
        Page<Product> productPage = productRepository.findAll(specification, pageable);
        productList = productPage.getContent();
        productList.stream()
                .map(product -> modelMapper.map(product, Product.class))
                .toList();
        return new PagingResponse<>(productList, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_SUCCESS), productPage.getTotalPages(), productPage.getTotalElements());
    }

    @Override
    public PagingResponse<List<Product>> findAllProductPublic(Map<Object, String> filters) {
        List<Product> productList;
        Pageable pageable;
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        String order = filters.get("order");
        if (page == -1 && limit == -1 ) {
            productList = productRepository.findAllByStatus(1);
            productList.stream()
                    .map(product -> modelMapper.map(product, Product.class))
                    .toList();
            return new PagingResponse<>(productList, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_SUCCESS), 1, (long) productList.size());
        } else {
            page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1);
            pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        }
        if (StringUtils.hasText(order)) {
            String[] orderParams = order.split("-");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(direction, orderParams[0])));
            }
        }
        Specification<Product> productSpecification = ProductSpecification.filterProducts(filters.get("productType"), "1", filters.get("search"));
        Page<Product> productPage = productRepository.findAll(productSpecification, pageable);
        productList = productPage.getContent();
        productList.stream()
                .map(product -> modelMapper.map(product, Product.class))
                .toList();
        return new PagingResponse<>(productList, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_SUCCESS), productPage.getTotalPages(), productPage.getTotalElements());
    }

    @Override
    public APIResponse<Product> updateProduct(ProductDTO product, String productId) {
        Product product1 = productRepository.findById(UUID.fromString(productId)).get();
        ProductType productType = productTypeRepository.findById(UUID.fromString(product.getType())).get();
        modelMapper.map(product,product1);
        product1.setProductType(productType);
        productRepository.save(product1);
        return new APIResponse<>(product1, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_UPDATE_SUCCESS));
    }

    @Override
    public APIResponse<Boolean> deleteProduct(String productId) {
        productRepository.deleteById(UUID.fromString(productId));
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_DELETE_SUCCESS));
    }

    @Override
    public APIResponse<Boolean> deleteMultiProduct(ProductMultiDeleteDTO productMultiDeleteDTO) {
        List<String> productListIds = productMultiDeleteDTO.getProductIds();
        productListIds.forEach(item -> {
            productRepository.deleteById(UUID.fromString(item));
        });
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_DELETE_SUCCESS));
    }

    @Override
    public Product getOneProduct(String productId) {
        return productRepository.findById(UUID.fromString(productId)).get();
    }

    @Override
    public Product getOneProductPublic(String productId, Boolean isViewed) {
        Product product = productRepository.findById(UUID.fromString(productId)).get();
//        if(isViewed)
        return product;
    }


    @Transactional
    public CloudinaryResponse uploadImage(String productId, final MultipartFile file) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(UUID.fromString(productId));
            if(optionalProduct.isEmpty()) throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED));
            Product product = optionalProduct.get();
            FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
            final String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
            final CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);
            product.setImage(response.getUrl());
            productRepository.save(product);
            return response;
        } catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
}
