package com.Optimart.services.Review;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Review.DeleteMultiReviewDTO;
import com.Optimart.dto.Review.UpdateReviewDTO;
import com.Optimart.dto.Review.WriteReviewDTO;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.OrderItem;
import com.Optimart.models.Product;
import com.Optimart.models.Review;
import com.Optimart.models.User;
import com.Optimart.repositories.*;
import com.Optimart.repositories.Specification.ReviewSpecification;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.responses.Product.BaseProductResponse;
import com.Optimart.responses.Review.ReviewResponse;
import com.Optimart.responses.User.BaseUserResponse;
import com.Optimart.utils.JwtTokenUtil;
import com.Optimart.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService{
    private final ModelMapper modelMapper;
    private final LocalizationUtils localizationUtils;
    private final JwtTokenUtil jwtTokenUtil;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    @Override
    @Transactional
    public APIResponse<ReviewResponse> writeNewReview(WriteReviewDTO writeReviewDTO) {
        Review review = modelMapper.map(writeReviewDTO, Review.class);
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(writeReviewDTO.getUserId()));
        if(optionalUser.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST));
        review.setUser(optionalUser.get());
        OrderItem orderItem = orderItemRepository.findById(UUID.fromString(writeReviewDTO.getProductId())).get();
        Optional<Product> optionalProduct = productRepository.findBySlug(orderItem.getSlug());
        if(optionalProduct.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED));
        review.setProduct(optionalProduct.get());
        reviewRepository.save(review);
        Product product = optionalProduct.get();
        List<Review> reviewList = product.getReviewList();
        reviewList.add(review);
        product.setReviewList(reviewList);
        product.setAverageRating(getAvgRatingProduct(reviewList));
        productRepository.save(product);
        ReviewResponse reviewResponse = ConvertToResponse(review);
        return new APIResponse<>(reviewResponse, localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_WRITE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<ReviewResponse> updateReview(UpdateReviewDTO updateReviewDTO, String reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(UUID.fromString(reviewId));
        if(optionalReview.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_NOT_EXISTED));
        Review review = optionalReview.get();
        Product product = review.getProduct();
        List<Review> reviewList = product.getReviewList();
        reviewList.remove(review);
        modelMapper.map(updateReviewDTO, review);
        review.setId(UUID.fromString(reviewId));
        reviewRepository.save(review);
        reviewList.add(review);
        product.setReviewList(reviewList);
        product.setAverageRating(getAvgRatingProduct(reviewList));
        productRepository.save(product);
        ReviewResponse reviewResponse = ConvertToResponse(review);
        return new APIResponse<>(reviewResponse, localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_GET_SUCCESS));
    }

    @Override
    public APIResponse<ReviewResponse> getOneReview(String reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(UUID.fromString(reviewId));
        if(optionalReview.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_NOT_EXISTED));
        ReviewResponse reviewResponse = ConvertToResponse(optionalReview.get());
        return new APIResponse<>(reviewResponse, localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_GET_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteOneReview(String reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(UUID.fromString(reviewId));
        if(optionalReview.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_NOT_EXISTED));
        Review review = optionalReview.get();
        Product product = review.getProduct();
        List<Review> reviewList = product.getReviewList();
        reviewList.remove(review);
        product.setReviewList(reviewList);
        product.setAverageRating(getAvgRatingProduct(reviewList));
        productRepository.save(product);
        reviewRepository.delete(review);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_DELETE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteOneReviewByMe(String reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(UUID.fromString(reviewId));
        if(optionalReview.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_NOT_EXISTED));
        Review review = optionalReview.get();
        User user = review.getUser();
        user.getReviewList().remove(review);

        Product product = review.getProduct();
        List<Review> reviewList = product.getReviewList();
        reviewList.remove(review);
        product.setReviewList(reviewList);
        product.setAverageRating(getAvgRatingProduct(reviewList));
        productRepository.save(product);

        reviewRepository.delete(review);
        userRepository.save(user);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_DELETE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteMultiReview(DeleteMultiReviewDTO deleteMultiReviewDTO) {
        List<String> reviewIds = deleteMultiReviewDTO.getReviewIds();
        List<UUID> reviewUUIDs = reviewIds.stream()
                .map(UUID::fromString)
                .toList();
        List<Review> reviews = reviewRepository.findAllById(reviewUUIDs);

        Map<Product, List<Review>> productReviewMap = new HashMap<>();
        for (Review review : reviews) {
            Product product = review.getProduct();
            productReviewMap
                    .computeIfAbsent(product, k -> new ArrayList<>())
                    .add(review);
        }
        productReviewMap.forEach((product, reviewList) -> {
            reviewList.forEach(product.getReviewList()::remove);
            product.setAverageRating(getAvgRatingProduct(product.getReviewList()));
        });
        productRepository.saveAll(productReviewMap.keySet());
        reviewRepository.deleteAll(reviews);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_DELETE_SUCCESS));
    }

    @Override
    public PagingResponse<List<ReviewResponse>> getAllReview(Map<Object, String> filters) {
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        String search = filters.getOrDefault("search", "");
        String order = filters.get("order");
        String productId = filters.getOrDefault("productId", "");
        if (page == -1 && limit == -1 ) {
            Specification<Review> specification = ReviewSpecification.filterReviewsByProductId(productId);
            List<Review> reviews = reviewRepository.findAll(specification);
            List<ReviewResponse> reviewResponseList = ConvertToResponse(reviews);
            return new PagingResponse<>(reviewResponseList, localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_LIST_GET_SUCCESS), 1, (long) reviews.size());
        }
        page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1) - 1;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Specification<Review> specification = ReviewSpecification.filterReviews(search);
        Page<Review> reviewPage = reviewRepository.findAll(specification, pageable);
        List<ReviewResponse> reviewResponseList = ConvertToResponse(reviewPage.getContent());
        return new PagingResponse<>(reviewResponseList, localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_LIST_GET_SUCCESS), reviewPage.getTotalPages(), reviewPage.getTotalElements());
    }

    private User getUser(String token){
        String jwtToken = token.substring(7);
        String email = jwtTokenUtil.extractEmail(jwtToken);
        Optional<User> optionalUser = authRepository.findByEmail(email);
        if(optionalUser.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST));
        return optionalUser.get();
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

    private List<ReviewResponse> ConvertToResponse(List<Review> review){
        return review.stream().map(
                this::ConvertToResponse
        ).toList();
    }

    private ReviewResponse ConvertToResponse(Review review){
        BaseUserResponse baseUserResponse = modelMapper.map(review.getUser(), BaseUserResponse.class);
        BaseProductResponse baseProductResponse = modelMapper.map(review.getProduct(), BaseProductResponse.class);
        ReviewResponse reviewResponse = modelMapper.map(review, ReviewResponse.class);
        reviewResponse.setUser(baseUserResponse);
        reviewResponse.setProduct(baseProductResponse);
        return reviewResponse;
    }

    private double getAvgRatingProduct(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }
        double totalStars = 0;
        for (Review review : reviews) {
            totalStars += review.getStar();
        }
        return totalStars / reviews.size();
    }

}
