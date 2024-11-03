package com.Optimart.services.Report;

import com.Optimart.responses.APIResponse;
import com.Optimart.responses.StatisticResponse.ModelResponse;
import com.Optimart.responses.StatisticResponse.ProductStatusResponse;
import com.Optimart.responses.StatisticResponse.ProductTypeResponse;
import com.Optimart.responses.StatisticResponse.UserTypeResponse;
import com.Optimart.responses.StatisticsResponse;

import java.util.List;

public interface IReportService {
    StatisticsResponse<UserTypeResponse> getUserTypeCount();
    StatisticsResponse<ProductStatusResponse> getProductStatusCount();
    APIResponse<ModelResponse> getModelStatistics();
    StatisticsResponse<List<ProductTypeResponse>> getProductTypeStats();
}
