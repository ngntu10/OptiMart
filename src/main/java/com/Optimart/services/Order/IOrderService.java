package com.Optimart.services.Order;

import com.Optimart.dto.Order.ChangeOrderStatus;
import com.Optimart.dto.Order.CreateOrderDTO;
import com.Optimart.models.Order;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Order.OrderResponse;
import com.Optimart.responses.PagingResponse;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;
import java.util.Map;

public interface IOrderService {
    APIResponse<OrderResponse> createOrder(CreateOrderDTO createOrderDTO) throws FirebaseMessagingException;
    PagingResponse<List<Order>> getAllOrderByMe(Map<Object, String> filters, String token);
    PagingResponse<List<Order>> getAllOrder(Map<Object, String> filters);
    APIResponse<OrderResponse> cancelOrder(String id) throws FirebaseMessagingException;
    APIResponse<Order> getOneOrderById(String id);
    APIResponse<Boolean> deleteOrder(String id);
    APIResponse<OrderResponse> changeStatusOrder(String orderId, ChangeOrderStatus changeOrderStatus) throws FirebaseMessagingException;
}
