package com.Optimart.services.Report;

import com.Optimart.constants.MessageKeys;
import com.Optimart.models.Order;
import com.Optimart.models.Product;
import com.Optimart.models.ProductType;
import com.Optimart.repositories.*;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.StatisticResponse.*;
import com.Optimart.responses.StatisticsResponse;
import com.Optimart.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedHashMap;
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
    public APIResponse<List<RevenueResponse>> getRevenueStatistics() {
        LocalDate currentDate = LocalDate.now();
        List<Order> orders = orderRepository.findAll();
        Map<YearMonth, Long> revenueMap = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            revenueMap.put(YearMonth.from(currentDate.minusMonths(i)), 0L);
        }
        orders.stream()
                .filter(order -> order.getOrderStatus() == 2 && order.getDeliveryAt() != null)
                .forEach(order -> {
                    YearMonth month = YearMonth.from(order.getDeliveryAt().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate());
                    revenueMap.computeIfPresent(month, (k, v) -> v + order.getTotalPrice());
                });
        List<RevenueResponse> revenueResponses = revenueMap.entrySet().stream()
                .map(entry -> new RevenueResponse(entry.getValue(), (long) entry.getKey().getMonthValue(), (long) entry.getKey().getYear()))
                .collect(Collectors.toList());
        return new APIResponse<>(revenueResponses, localizationUtils.getLocalizedMessage(MessageKeys.GET_STATISTICS));
    }

    @Override
    public APIResponse<StatisticsResponse<OrderStatusStats>> getOrderStatusStat() {
        Long stt0 = orderRepository.countByOrderStatus(0);
        Long stt1 = orderRepository.countByOrderStatus(1);
        Long stt2 = orderRepository.countByOrderStatus(2);
        Long stt3 = orderRepository.countByOrderStatus(3);
        Long total = orderRepository.count();
        OrderStatusStats orderStatusStats = OrderStatusStats.builder()
                .waitDelivery(stt0)
                .waitPayment(stt1)
                .doneOrder(stt2)
                .cancelOrder(stt3)
                .build();
        StatisticsResponse<OrderStatusStats> statisticsResponse = new StatisticsResponse<>(orderStatusStats, total);
        return new APIResponse<>(statisticsResponse, localizationUtils.getLocalizedMessage(MessageKeys.GET_STATISTICS));
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
