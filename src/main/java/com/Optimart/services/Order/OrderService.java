package com.Optimart.services.Order;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Order.CreateOrderDTO;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.*;
import com.Optimart.repositories.*;
import com.Optimart.repositories.Specification.OrderSpecification;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Order.OrderResponse;
import com.Optimart.responses.PagingResponse;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final OrderItemRepository orderItemRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final LocalizationUtils localizationUtils;
    @Override
    public APIResponse<OrderResponse> createOrder(CreateOrderDTO createOrderDTO) {
        Order order = modelMapper.map(createOrderDTO, Order.class);
        User user = userRepository.findById(UUID.fromString(createOrderDTO.getUserId())).get();
        order.setUser(user);
        ShippingAddress shippingAddress = modelMapper.map(createOrderDTO, ShippingAddress.class);
        shippingAddressRepository.save(shippingAddress);
        Paymenttype paymenttype = paymentTypeRepository.findById(UUID.fromString(createOrderDTO.getPaymentMethod())).get();
        DeliveryType deliveryType = deliveryTypeRepository.findById(UUID.fromString(createOrderDTO.getDeliveryMethod())).get();
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymenttype);
        order.setDeliveryMethod(deliveryType);
        order.setOrderStatus(1);
        order.setUser(user);
        order = orderRepository.save(order);
        Order finalOrder = order;
        List<OrderItem> orderItems = createOrderDTO.getOrderItems().stream()
                .map(item -> {
                    OrderItem orderItem = modelMapper.map(item, OrderItem.class);
                    orderItem.setOrder(finalOrder);
                    return orderItem;
                }).collect(Collectors.toList());
        orderItemRepository.saveAll(orderItems);
        order.setOrderItemList(orderItems);
        orderRepository.save(order);
        List<Order> orders = user.getOrderList();
        orders.add(order);
        user.setOrderList(orders);
        userRepository.save(user);
        return new APIResponse<>(new OrderResponse(order.getId()), localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CREATE_SUCCESS));
    }

    @Override
    public PagingResponse<List<Order>> getAllOrderByMe(Map<Object, String> filters, String token) {
        User user = getUser(token);
        List<Order> orderList = user.getOrderList();
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        String order = filters.get("order");
        if (page == -1 && limit == -1 ) {
            return new PagingResponse<>(orderList, localizationUtils.getLocalizedMessage(MessageKeys.ORDER_LIST_GET_SUCCESS), 1, (long) orderList.size());
        }
        page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1) - 1;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Specification<Order> specification = OrderSpecification.filterOrderByUser(filters.get("status"), user.getId());
        Page<Order> orderPage = orderRepository.findAll(specification, pageable);
        return new PagingResponse<>(orderPage.getContent(), localizationUtils.getLocalizedMessage(MessageKeys.ORDER_LIST_GET_SUCCESS), orderPage.getTotalPages(), orderPage.getTotalElements());
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

    @Override
    public PagingResponse<List<Order>> getAllOrder(Map<Object, String> filters ) {
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        String order = filters.get("order");
        if (page == -1 && limit == -1 ) {
            List<Order> orderList = orderRepository.findAll();
            return new PagingResponse<>(orderList, localizationUtils.getLocalizedMessage(MessageKeys.ORDER_LIST_GET_SUCCESS), 1, (long) orderList.size());
        }
        page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1) - 1;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Page<Order> orderPage = orderRepository.findAll(pageable);
//        Specification<Product> specification = ProductSpecification.filterProducts(filters.get("productType"), filters.get("status"), filters.get("search"));
        return new PagingResponse<>(orderPage.getContent(), localizationUtils.getLocalizedMessage(MessageKeys.ORDER_LIST_GET_SUCCESS), orderPage.getTotalPages(), orderPage.getTotalElements());
    }

    private User getUser(String token){
        String jwtToken = token.substring(7);
        String email = jwtTokenUtil.extractEmail(jwtToken);
        Optional<User> optionalUser = authRepository.findByEmail(email);
        if(optionalUser.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST));
        return optionalUser.get();
    }
}
