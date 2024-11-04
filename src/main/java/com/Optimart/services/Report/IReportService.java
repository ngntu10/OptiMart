package com.Optimart.services.Report;

import com.Optimart.responses.APIResponse;
import com.Optimart.responses.StatisticResponse.*;
import com.Optimart.responses.StatisticsResponse;

import java.util.List;

public interface IReportService {
    StatisticsResponse<UserTypeResponse> getUserTypeCount();
    StatisticsResponse<ProductStatusResponse> getProductStatusCount();
    StatisticsResponse<List<ProductTypeResponse>> getProductTypeStats();
    APIResponse<ModelResponse> getModelStatistics();
    APIResponse<List<RevenueResponse>> getRevenueStatistics();
    APIResponse<StatisticsResponse<OrderStatusStats>> getOrderStatusStat();
}
