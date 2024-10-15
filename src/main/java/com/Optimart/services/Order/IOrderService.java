package com.Optimart.services.Order;

import com.Optimart.dto.Order.CreateOrderDTO;
import com.Optimart.models.Order;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Order.OrderResponse;
import com.Optimart.responses.PagingResponse;

import java.util.List;
import java.util.Map;

public interface IOrderService {
    APIResponse<OrderResponse> createOrder(CreateOrderDTO createOrderDTO);
    PagingResponse<List<Order>> getAllOrderByMe(Map<Object, String> filters, String token);
    PagingResponse<List<Order>> getAllOrder(Map<Object, String> filters);
}
