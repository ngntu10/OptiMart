package com.Optimart.services.Product;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Product.CreateProductDTO;
import com.Optimart.dto.Product.ProductDTO;
import com.Optimart.dto.Product.ProductMultiDeleteDTO;
import com.Optimart.dto.Product.ReactionProductDTO;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.City;
import com.Optimart.models.Product;
import com.Optimart.models.ProductType;
import com.Optimart.models.User;
import com.Optimart.repositories.AuthRepository;
import com.Optimart.repositories.CityLocaleRepository;
import com.Optimart.repositories.ProductRepository;
import com.Optimart.repositories.ProductTypeRepository;
import com.Optimart.repositories.Specification.ProductSpecification;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.CloudinaryResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.services.CloudinaryService;
import com.Optimart.utils.FileUploadUtil;
import com.Optimart.utils.JwtTokenUtil;
import com.Optimart.utils.LocalizationUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final LocalizationUtils localizationUtils;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final CityLocaleRepository cityLocaleRepository;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final AuthRepository authRepository;
    @Override
    public APIResponse<Product> createProduct(CreateProductDTO createProductDTO) {
        Product product = modelMapper.map(createProductDTO, Product.class);
        ProductType productType = productTypeRepository.findById(UUID.fromString(createProductDTO.getType())).get();
        product.setProductType(productType);
        productRepository.save(product);
        return new APIResponse<>(product, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_CREATE_SUCCESS));
    }

    @Override
    public APIResponse<Boolean> likeProduct(ReactionProductDTO reactionProductDTO, String token) {
        String jwtToken = token.substring(7);
        String email = jwtTokenUtil.extractEmail(jwtToken);
        Optional<User> optionalUser = authRepository.findByEmail(email);
        if(optionalUser.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST));
        User user = optionalUser.get();

        String productId = reactionProductDTO.getProductId();
        Set<Product> productList = user.getLikeProductList();
        Optional<Product> optionalProduct = productRepository.findById(UUID.fromString(productId));
        Product product = optionalProduct.get();
        productList.add(product);

        Set<User> userList = product.getUserLikedList();
        userList.add(user);
        product.setUserLikedList(userList);
        productRepository.save(product);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_LIKED));
    }

    @Override
    @Transactional(readOnly = true)
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
        }
        page -= 1;
        pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Specification<Product> specification = ProductSpecification.filterProducts(filters.get("productType"), filters.get("status"), filters.get("search"));;
        return getListPagingResponse(pageable, specification);
    }

    private Pageable getPageable(Pageable pageable, int page, int limit, String order) {
        if (StringUtils.hasText(order)) {
            String[] orderParams = order.split(" ");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(direction, orderParams[0])));
            }
        }
        return pageable;
    }

    @NotNull
    private PagingResponse<List<Product>> getListPagingResponse(Pageable pageable, Specification<Product> specification) {
        List<Product> productList;
        Page<Product> productPage = productRepository.findAll(specification, pageable);
        productList = productPage.getContent();
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
            page = Math.max(Integer.parseInt(filters.getOrDefault("page", "1")), 1);
        }
        page -= 1;
        pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Specification<Product> productSpecification = ProductSpecification.filterProducts(filters.get("productType"), "1", filters.get("search"));
        return getListPagingResponse(pageable, productSpecification);
    }

    @Override
    public PagingResponse<List<Product>> getListProductRelatedTo(Map<Object, String> filters) {
        Product product1 = productRepository.findBySlug(filters.get("slug")).get();
        ProductType type = product1.getProductType();
        List<Product> productList;
        Pageable pageable;
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        String order = filters.get("order");
        if (page == -1 && limit == -1 ) {
            productList = productRepository.findAll();
            List<Product> products =  productList.stream()
                    .filter(product -> product.getProductType().getName().equals(type.getName())
                            && product.getStatus() == 1)
                    .map(item -> modelMapper.map(item, Product.class))
                    .collect(Collectors.toList());
            products.remove(product1);
            return new PagingResponse<>(products, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_SUCCESS), 1, (long) productList.size());
        } else {
            page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1);
        }
        page -= 1;
        pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Specification<Product> specification = ProductSpecification.filterProducts(type.getName(), "1", filters.get("search"));;
        return getListPagingResponse(pageable, specification);
    }

    @Override
    public APIResponse<Product> updateProduct(ProductDTO product, String productId) {
        Product product1 = productRepository.findById(UUID.fromString(productId)).get();
        City city = cityLocaleRepository.findById(Long.parseLong(product.getLocation())).get();
        ProductType productType = productTypeRepository.findById(UUID.fromString(product.getType())).get();
        modelMapper.map(product,product1);
        product1.setProductType(productType);
        product1.setCity(city);
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
    public Product getOneProductBySlug(String slug) {
        return productRepository.findBySlug(slug).get();
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
