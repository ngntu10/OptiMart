package com.Optimart.services.Order;

import com.Optimart.dto.Order.CreateOrderDTO;
import com.Optimart.models.Order;
import com.Optimart.responses.APIResponse;

public interface IOrderService {
    APIResponse<Order> createOrder(CreateOrderDTO createOrderDTO);
}
