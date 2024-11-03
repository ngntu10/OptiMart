package com.Optimart.services.Report;

import com.Optimart.constants.MessageKeys;
import com.Optimart.models.Order;
import com.Optimart.models.Product;
import com.Optimart.models.ProductType;
import com.Optimart.repositories.*;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.StatisticResponse.ModelResponse;
import com.Optimart.responses.StatisticResponse.ProductStatusResponse;
import com.Optimart.responses.StatisticResponse.ProductTypeResponse;
import com.Optimart.responses.StatisticResponse.UserTypeResponse;
import com.Optimart.responses.StatisticsResponse;
import com.Optimart.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService{
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final ProductTypeRepository productTypeRepository;
    private final LocalizationUtils localizationUtils;
    @Override
    public StatisticsResponse<UserTypeResponse> getUserTypeCount() {
        Long googleUser = userRepository.countByGoogleAccountIdIsNotNull();
        Long facebookUser = userRepository.countByFacebookAccountIdIsNotNull();
        Long totalUser = userRepository.count();
        Long emailUser = totalUser - googleUser - facebookUser;
        UserTypeResponse userTypeResponse = UserTypeResponse.builder()
                .googleUser(googleUser)
                .facebookUser(facebookUser)
                .emailUser(emailUser)
                .build();
        return new StatisticsResponse<>(userTypeResponse, totalUser);
    }

    @Override
    public StatisticsResponse<ProductStatusResponse> getProductStatusCount() {
        Long publicProduct = productRepository.countProductByStatus(1);
        Long privateProduct = productRepository.countProductByStatus(0);
        Long total = productRepository.count();
        ProductStatusResponse productStatusResponse = ProductStatusResponse.builder()
                .privateProduct(privateProduct)
                .publicProduct(publicProduct)
                .build();
        return new StatisticsResponse<>(productStatusResponse, total);
    }

    @Override
    public APIResponse<ModelResponse> getModelStatistics() {
        Long userCount = userRepository.count();
        Long commentCount = commentRepository.count();
        Long productCount = productRepository.count();
        Long orderCount = orderRepository.count();
        Long reviewCount = reviewRepository.count();
        List<Order> orders = orderRepository.findAll();
        Long revenueCount = orders.stream()
                .filter(order -> order.getIsPaid() == 1 && order.getOrderStatus() == 2)
                .mapToLong(Order::getTotalPrice)
                .sum();
        ModelResponse modelResponse = ModelResponse.builder()
                .user(userCount)
                .product(productCount)
                .comment(commentCount)
                .order(orderCount)
                .revenue(revenueCount)
                .review(reviewCount)
                .build();
        return new APIResponse<>(modelResponse, localizationUtils.getLocalizedMessage(MessageKeys.GET_STATISTICS));
    }

    @Override
    public StatisticsResponse<List<ProductTypeResponse>> getProductTypeStats() {
        List<Product> productList = productRepository.findAll();
        List<ProductType> productTypeList = productTypeRepository.findAll();
        Map<String, Long> productCountByType = productList.stream()
                .collect(Collectors.groupingBy(
                        product -> product.getProductType().getName(),
                        Collectors.counting()
                ));
        List<ProductTypeResponse> responseList = productTypeList.stream()
                .map(productType -> {
                    ProductTypeResponse response = new ProductTypeResponse();
                    response.setTypeName(productType.getName());
                    response.setTotal(productCountByType.getOrDefault(productType.getName(), 0L));
                    return response;
                })
                .collect(Collectors.toList());
        return new StatisticsResponse<>(responseList, 0L);
    }
}
