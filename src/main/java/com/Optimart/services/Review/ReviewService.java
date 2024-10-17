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
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
        ReviewResponse reviewResponse = ConvertToResponse(review);
        return new APIResponse<>(reviewResponse, localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_WRITE_SUCCESS));
    }

    @Override
    public APIResponse<ReviewResponse> updateReview(UpdateReviewDTO updateReviewDTO, String reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(UUID.fromString(reviewId));
        if(optionalReview.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_NOT_EXISTED));
        Review review = optionalReview.get();
        modelMapper.map(updateReviewDTO, review);
        review.setId(UUID.fromString(reviewId));
        reviewRepository.save(review);
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
    public APIResponse<Boolean> deleteOneReview(String reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(UUID.fromString(reviewId));
        if(optionalReview.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_NOT_EXISTED));
        Review review = optionalReview.get();
        reviewRepository.delete(review);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_DELETE_SUCCESS));
    }

    @Override
    public APIResponse<Boolean> deleteOneReviewByMe(String reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(UUID.fromString(reviewId));
        if(optionalReview.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_NOT_EXISTED));
        Review review = optionalReview.get();
        User user = review.getUser();
        user.getReviewList().remove(review);
        reviewRepository.delete(review);
        userRepository.save(user);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_DELETE_SUCCESS));
    }

    @Override
    public APIResponse<Boolean> deleteMultiReview(DeleteMultiReviewDTO deleteMultiReviewDTO) {
        List<String> reviewIds = deleteMultiReviewDTO.getReviewIds();
        List<UUID> reviewUUIDs = reviewIds.stream().map(
                item ->{
                    UUID uuid = UUID.fromString(item);
                    return uuid;
                }
        ).toList();
        reviewRepository.deleteAllById(reviewUUIDs);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.REVIEW_DELETE_SUCCESS));
    }

    @Override
    public PagingResponse<List<ReviewResponse>> getAllReview(Map<Object, String> filters) {
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        String search = filters.getOrDefault("search", "");
        String order = filters.get("order");
        if (page == -1 && limit == -1 ) {
            List<Review> reviews = reviewRepository.findAll();
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
                item -> {
                    BaseUserResponse baseUserResponse = modelMapper.map(item.getUser(), BaseUserResponse.class);
                    BaseProductResponse baseProductResponse = modelMapper.map(item.getProduct(), BaseProductResponse.class);
                    return ReviewResponse.builder()
                            .star(item.getStar()).content(item.getContent()).id(item.getId())
                            .product(baseProductResponse).user(baseUserResponse).build();
                }
        ).toList();
    }

    private ReviewResponse ConvertToResponse(Review review){
        BaseUserResponse baseUserResponse = modelMapper.map(review.getUser(), BaseUserResponse.class);
        BaseProductResponse baseProductResponse = modelMapper.map(review.getProduct(), BaseProductResponse.class);
        return ReviewResponse.builder()
                .star(review.getStar()).content(review.getContent()).id(review.getId())
                .product(baseProductResponse).user(baseUserResponse).build();
    }
}
