package com.Optimart.services.Product;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Product.*;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.*;
import com.Optimart.repositories.*;
import com.Optimart.repositories.Specification.ProductSpecification;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.CloudinaryResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.responses.Product.BaseProductResponse;
import com.Optimart.responses.Product.ProductResponse;
import com.Optimart.responses.Review.ReviewResponse;
import com.Optimart.responses.User.BaseUserResponse;
import com.Optimart.services.Cloudinary.CloudinaryService;
import com.Optimart.services.Redis.Product.ProductRedisService;
import com.Optimart.utils.FileUploadUtil;
import com.Optimart.utils.JwtTokenUtil;
import com.Optimart.utils.LocalizationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private final ProductRedisService productRedisService;
    private final ModelMapper modelMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final CityLocaleRepository cityLocaleRepository;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final AuthRepository authRepository;
    @Override
    @Transactional
    public APIResponse<Product> createProduct(CreateProductDTO createProductDTO) {
        Product product = modelMapper.map(createProductDTO, Product.class);
        City city = cityLocaleRepository.findById(Long.parseLong(createProductDTO.getLocation()))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.CITY_NOT_FOUND)));
        ProductType productType = productTypeRepository.findById(UUID.fromString(createProductDTO.getType()))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_TYPE_NOT_FOUND)));
        product.setProductType(productType);
        product.setCity(city);
        productRepository.save(product);
        return new APIResponse<>(product, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_CREATE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> likeProduct(ReactionProductDTO reactionProductDTO, String token) {
        User user = getUser(token);
        String productId = reactionProductDTO.getProductId();
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED)));;
        List<Product> productList = user.getLikeProductList();
        productList.add(product);

        user.setLikeProductList(productList);
        userRepository.save(user);

        List<User> userList = product.getUserLikedList();
        userList.add(user);
        product.setUserLikedList(userList);
        product.setTotalLikes(product.getUserLikedList().size());
        productRepository.save(product);

        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_LIKED));
    }


    private User getUser(String token){
        String jwtToken = token.substring(7);
        String email = jwtTokenUtil.extractEmail(jwtToken);
        Optional<User> optionalUser = authRepository.findByEmail(email);
        if(optionalUser.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST));
        return optionalUser.get();
    }

    @Override
    @Transactional
    public APIResponse<Boolean> unlikeProduct(ReactionProductDTO reactionProductDTO, String token) {
        User user = getUser(token);

        String productId = reactionProductDTO.getProductId();
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED)));
        List<Product> productList = user.getLikeProductList();
        productList.remove(product);
        user.setLikeProductList(productList);
        userRepository.save(user);

        List<User> userList = product.getUserLikedList();
        userList.remove(user);
        product.setUserLikedList(userList);
        product.setTotalLikes(product.getUserLikedList().size());
        productRepository.save(product);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_UNLIKED));
    }

    @Override
    public PagingResponse<List<ProductResponse>> findAllProduct(Map<Object, String> filters) throws JsonProcessingException {
        List<Product> productList;
        Pageable pageable;
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        String order = filters.get("order");
        if (page == -1 && limit == -1 ) {
            productList = productRepository.findAll();
            List<ProductResponse> productResponseList = productList.stream()
                    .map(product -> modelMapper.map(product, ProductResponse.class))
                    .toList();
            productRedisService.saveAllProducts(productResponseList, filters);
            return new PagingResponse<>(productResponseList, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_SUCCESS), 1, (long) productList.size());
        }
        page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1) - 1;
        pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Specification<Product> specification = ProductSpecification.filterProducts(filters.get("productType"), filters.get("status"), filters.get("search"), null, null);
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
    private PagingResponse<List<ProductResponse>> getListPagingResponse(Pageable pageable, Specification<Product> specification) {
        List<Product> productList;
        Page<Product> productPage = productRepository.findAll(specification, pageable);
        productList = productPage.getContent();
        List<ProductResponse> productResponseList = productList.stream()
                .map(product -> {
                    ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
                    Set<UUID> userLikeList = product.getUserLikedList().stream()
                            .map(User::getId)
                            .collect(Collectors.toSet());
                    List<ReviewResponse> responses = ConvertToResponse(product.getReviewList());
                    productResponse.setReviewList(responses);
                    productResponse.setUserLikedList(userLikeList);
                    return productResponse;
                })
                .toList();
        return new PagingResponse<>(productResponseList, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_SUCCESS), productPage.getTotalPages(), productPage.getTotalElements());
    }

    @Override
    public PagingResponse<List<ProductResponse>> findAllProductPublic(Map<Object, String> filters) {
        List<Product> productList;
        Pageable pageable;
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        Long cityId = Optional.ofNullable(filters.get("productLocation"))
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .orElse(null);
        Double minStar = Optional.ofNullable(filters.get("minStar"))
                .filter(s -> !s.isEmpty())
                .map(Double::parseDouble)
                .orElse(null);
        String order = filters.get("order");
        if (page == -1 && limit == -1 ) {
            productList = productRepository.findAllByStatus(1);
            List<ProductResponse> productResponseList = productList.stream()
                    .map(product -> modelMapper.map(product, ProductResponse.class))
                    .toList();
            return new PagingResponse<>(productResponseList, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_SUCCESS), 1, (long) productList.size());
        }
        page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1) - 1;
        pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Specification<Product> productSpecification = ProductSpecification.filterProducts(filters.get("productType"), "1", filters.get("search"), cityId, minStar);
        return getListPagingResponse(pageable, productSpecification);
    }

    @Override
    public PagingResponse<List<ProductResponse>> getListProductRelatedTo(Map<Object, String> filters) {
        Product product1 = productRepository.findBySlug(filters.get("slug"))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED)));
        ProductType type = product1.getProductType();
        List<Product> productList;
        Pageable pageable;
//        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
//        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        int page = 1;
        int limit = 3;
        String order = filters.get("order");
        if (page == -1 && limit == -1 ) {
            productList = productRepository.findAll();
            List<ProductResponse> products =  productList.stream()
                    .filter(product -> product.getProductType().getName().equals(type.getName())
                            && product.getStatus() == 1)
                    .map(item -> modelMapper.map(item, ProductResponse.class))
                    .collect(Collectors.toList());

            ProductResponse productResponseToRemove = modelMapper.map(product1, ProductResponse.class);
            products.removeIf(product -> product.getId().equals(productResponseToRemove.getId()));
            return new PagingResponse<>(products, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_SUCCESS), 1, (long) productList.size());
        }
        page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1) - 1;
        pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Specification<Product> specification = ProductSpecification.filterProducts(type.getId().toString(), "1", filters.get("search"), null , null);
        return getListPagingResponse(pageable, specification);
    }

    @Override
    public PagingResponse<List<ProductResponse>> getLikedProducts(Map<Object, String> filters, String token) {
        int page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1) - 1;
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        User user = getUser(token);
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Specification<Product> specification = ProductSpecification.getProductLikedByUser(user.getId(), filters.get("search"));
        return getListPagingResponse(pageable, specification);
    }

    @Override
    public PagingResponse<List<ProductResponse>> getViewedProducts(Map<Object, String> filters, String token) {
        int page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1) - 1;
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        User user = getUser(token);
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Specification<Product> specification = ProductSpecification.getProductViewedByUser(user.getId(), filters.get("search"));
        return getListPagingResponse(pageable, specification);
    }

    @Override
    @Transactional
    public APIResponse<Product> updateProduct(ProductDTO product, String productId) {
        Product product1 = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED)));
        City city = cityLocaleRepository.findById(Long.parseLong(product.getLocation()))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.CITY_NOT_FOUND)));
        ProductType productType = productTypeRepository.findById(UUID.fromString(product.getType()))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_TYPE_NOT_FOUND)));
        modelMapper.map(product, product1);
        product1.setProductType(productType);
        product1.setCity(city);
        productRepository.save(product1);
        return new APIResponse<>(product1, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_UPDATE_SUCCESS));

    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteProduct(String productId) {
        productRepository.deleteById(UUID.fromString(productId));
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_DELETE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteMultiProduct(ProductMultiDeleteDTO productMultiDeleteDTO) {
        List<String> productListIds = productMultiDeleteDTO.getProductIds();
        productListIds.forEach(item -> {
            productRepository.deleteById(UUID.fromString(item));
        });
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_DELETE_SUCCESS));
    }

    @Override
    public Product getOneProduct(String productId) {
        return productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED)));
    }

    @Override
    public ProductResponse getOneProductBySlug(String slug,  Map<String, String> params) {
        Boolean isViewed = Boolean.valueOf(params.get("isViewed"));
        String userId = params.get("userId");
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED)));
        List<ReviewResponse> responses = product.getReviewList().stream().map(
                this::ConvertToResponse
        ).toList();
        if(isViewed) product.setViews(product.getViews()+1);
        if(userId != null){
            User user = userRepository.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
            user.getViewedProductList().add(product);
            userRepository.save(user);
            product.getUserViewedList().add(user);
            productRepository.save(product);
        }
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        productResponse.setReviewList(responses);
        return productResponse;
    }

    private List<ReviewResponse> ConvertToResponse(List<Review> review){
        return review.stream().map(
                this::ConvertToResponse
        ).toList();
    }

    @Override
    public Product getOneProductPublic(String productId, Boolean isViewed) {
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED)));;
        return product;
    }

    private ReviewResponse ConvertToResponse(Review review){
        BaseUserResponse baseUserResponse = modelMapper.map(review.getUser(), BaseUserResponse.class);
        BaseProductResponse baseProductResponse = modelMapper.map(review.getProduct(), BaseProductResponse.class);
        return ReviewResponse.builder()
                .star(review.getStar()).content(review.getContent()).id(review.getId())
                .product(baseProductResponse).user(baseUserResponse).build();
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
