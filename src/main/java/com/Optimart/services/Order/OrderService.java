package com.Optimart.services.Order;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Order.CreateOrderDTO;
import com.Optimart.dto.OrderItem.OrderItemsDTO;
import com.Optimart.models.*;
import com.Optimart.repositories.DeliveryTypeRepository;
import com.Optimart.repositories.OrderRepository;
import com.Optimart.repositories.PaymentTypeRepository;
import com.Optimart.repositories.UserRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final UserRepository userRepository;
    private final LocalizationUtils localizationUtils;
    @Override
    public APIResponse<Order> createOrder(CreateOrderDTO createOrderDTO) {
        Order order = modelMapper.map(createOrderDTO, Order.class);
        User user = userRepository.findById(UUID.fromString(createOrderDTO.getUserId())).get();
        order.setUser(user);
        List<OrderItem> orderItems = createOrderDTO.getOrderItems().stream()
                .map(item -> {
                    OrderItem orderItem = modelMapper.map(item, OrderItem.class);
                    return orderItem;
                }).toList();
        order.setOrderItemList(orderItems);
        Paymenttype paymenttype = paymentTypeRepository.findById(UUID.fromString(createOrderDTO.getPaymentMethod())).get();
        DeliveryType deliveryType = deliveryTypeRepository.findById(UUID.fromString(createOrderDTO.getDeliveryMethod())).get();
        order.setPaymentMethod(paymenttype);
        order.setDeliveryMethod(deliveryType);
        orderRepository.save(order);
        return new APIResponse<>(order, localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CREATE_SUCCESS));
    }
}
