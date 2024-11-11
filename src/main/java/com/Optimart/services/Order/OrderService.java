package com.Optimart.services.Order;

import com.Optimart.constants.Context;
import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.FirebaseCloud.Note;
import com.Optimart.dto.Order.ChangeOrderStatus;
import com.Optimart.dto.Order.CreateOrderDTO;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.*;
import com.Optimart.repositories.*;
import com.Optimart.repositories.Specification.OrderSpecification;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Order.OrderResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.services.Firebase.FirebaseMessagingService;
import com.Optimart.utils.JwtTokenUtil;
import com.Optimart.utils.LocalizationUtils;
import com.google.firebase.FirebaseException;
import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final ProductRepository productRepository;
    private final NotificationRepository notificationRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final LocalizationUtils localizationUtils;
    private final FirebaseMessagingService firebaseMessagingService;
    @Override
    @Transactional
    public APIResponse<OrderResponse> createOrder(CreateOrderDTO createOrderDTO) {
        Order order = modelMapper.map(createOrderDTO, Order.class);
        User user = userRepository.findById(UUID.fromString(createOrderDTO.getUserId()))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
        order.setUser(user);
        ShippingAddress shippingAddress = modelMapper.map(createOrderDTO, ShippingAddress.class);
        shippingAddressRepository.save(shippingAddress);
        Paymenttype paymenttype = paymentTypeRepository.findById(UUID.fromString(createOrderDTO.getPaymentMethod()))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PAYMENT_TYPE_NOT_FOUND)));
        DeliveryType deliveryType = deliveryTypeRepository.findById(UUID.fromString(createOrderDTO.getDeliveryMethod()))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.DELIVERY_TYPE_NOT_FOUND)));
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymenttype);
        order.setDeliveryMethod(deliveryType);
        order.setOrderStatus(0);
        order.setUser(user);
        order = orderRepository.save(order);
        Order finalOrder = order;
        List<OrderItem> orderItems = createOrderDTO.getOrderItems().stream()
                .map(item -> {
                    OrderItem orderItem = modelMapper.map(item, OrderItem.class);
                    orderItem.setOrder(finalOrder);
                    Product productId = productRepository.findBySlug(orderItem.getSlug())
                            .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED)));
                    orderItem.setId(productId.getId());
                    return orderItem;
                }).collect(Collectors.toList());
        order.setOrderItemList(orderItems);
        orderRepository.save(order);
        List<Order> orders = user.getOrderList();
        orders.add(order);
        user.setOrderList(orders);
        Notification notification = Notification.builder()
                .context(Context.ORDER)
                .body(String.format("Đơn hàng với id %s đã được tạo thành công", order.getId().toString()))
                .users(Collections.singletonList(user))
                .referenceId(order.getId().toString())
                .title("Đã tạo đơn hàng")
                .build();

        user.getNotifications().add(notification);
        userRepository.save(user);
        try{
            firebaseMessagingService.sendNotification(new Note(Context.ORDER, localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CREATE_SUCCESS), localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CREATE_SUCCESS_ID, order.getId().toString())), user.getDeviceToken());
        } catch (FirebaseMessagingException e) {
            logger.error(e.getMessage());
        }
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
        Specification<Order> specification = OrderSpecification.filterOrderByUser(filters.get("status"), user.getId(), filters.get("cityId"), false);
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
        String search = filters.getOrDefault("search", "");
        String order = filters.get("order");
        if (page == -1 && limit == -1 ) {
            List<Order> orderList = orderRepository.findAll();
            return new PagingResponse<>(orderList, localizationUtils.getLocalizedMessage(MessageKeys.ORDER_LIST_GET_SUCCESS), 1, (long) orderList.size());
        }
        page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1) - 1;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Specification<Order> specification = OrderSpecification.filterOrder(filters.get("status"), filters.get("cityId"), search);
        Page<Order> orderPage = orderRepository.findAll(specification, pageable);
        return new PagingResponse<>(orderPage.getContent(), localizationUtils.getLocalizedMessage(MessageKeys.ORDER_LIST_GET_SUCCESS), orderPage.getTotalPages(), orderPage.getTotalElements());
    }

    @Override
    @Transactional
    public APIResponse<OrderResponse> cancelOrder(String id) throws FirebaseMessagingException {
        Order order = orderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_NOT_FOUND)));
        order.setOrderStatus(3);
        orderRepository.save(order);
        User user = order.getUser();
        Notification notification = Notification.builder()
                .context(Context.ORDER)
                .body(String.format("Đơn hàng với id %s đã được hủy thành công", order.getId().toString()))
                .users(Collections.singletonList(user))
                .referenceId(order.getId().toString())
                .title("Hủy đơn hàng thành công")
                .build();

        user.getNotifications().add(notification);
        userRepository.save(user);

        firebaseMessagingService.sendNotification(new Note(Context.ORDER, localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CANCEL_SUCCESS),
                localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CANCEL_SUCCESS_ID, order.getId().toString())),
                user.getDeviceToken());
        return new APIResponse<>(new OrderResponse(order.getId()), localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CANCEL_SUCCESS));
    }

    @Override
    public APIResponse<Order> getOneOrderById(String id) {
        Order order = orderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_NOT_FOUND)));
        return new APIResponse<>(order, MessageKeys.ORDER_GET_SUCCESS);
    }

    @Transactional
    public void handlePaymentOrderById(String orderId) throws FirebaseMessagingException {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_NOT_FOUND)));
        order.getOrderItemList().forEach(orderItem -> {
            Product product = productRepository.findBySlug(orderItem.getSlug())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED)));
            int amount = orderItem.getAmount();
            product.setSold(product.getSold() + amount);
            productRepository.save(product);
        });
        order.setOrderStatus(2);
        order.setIsPaid(1);
        order.setPaidAt(new Date());
        order.setIsDelivered(1);
        order.setDeliveryAt(new Date());
        orderRepository.save(order);

        // Push Noti
        User user = order.getUser();
        Notification notification = Notification.builder()
                .context(Context.ORDER)
                .body(String.format("Đơn hàng với id %s đã được thanh toán thành công", order.getId().toString()))
                .users(Collections.singletonList(user))
                .referenceId(order.getId().toString())
                .title("Thanh toán thành công")
                .build();

        user.getNotifications().add(notification);
        userRepository.save(user);

        firebaseMessagingService.sendNotification(new Note(Context.PAYMENT_VN_PAY, localizationUtils.getLocalizedMessage(MessageKeys.PAY_SUCCESS),
                        localizationUtils.getLocalizedMessage(MessageKeys.PAYMENT_SUCCESS_ID, order.getId().toString())), user.getDeviceToken());
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteOrder(String id) {
        Order order = orderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_NOT_FOUND)));
        orderRepository.delete(order);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.ORDER_DELETE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<OrderResponse> changeStatusOrder(String orderId, ChangeOrderStatus changeOrderStatus) throws FirebaseMessagingException {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_NOT_FOUND)));
        order.setIsDelivered(changeOrderStatus.getIsDelivered());
        order.setIsPaid(changeOrderStatus.getIsPaid());
        order.setOrderStatus(changeOrderStatus.getStatus());
        orderRepository.save(order);

        User user = order.getUser();
        Notification notification = Notification.builder()
                .context(Context.ORDER)
                .body(String.format("Đơn hàng với id %s đã được cập nhật trạng thái, click để xem chi tiết", order.getId().toString()))
                .users(Collections.singletonList(user))
                .referenceId(order.getId().toString())
                .title("Cập nhận đơn hàng")
                .build();

        user.getNotifications().add(notification);
        userRepository.save(user);
        List<Notification> notifications = user.getNotifications();
        notificationRepository.saveAll(notifications);

        firebaseMessagingService.sendNotification(new Note(Context.PAYMENT_VN_PAY, localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CHANGE_STATUS_SUCCESS),
                localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CHANGE_STATUS_ID, order.getId().toString())), user.getDeviceToken());

        return new APIResponse<>(new OrderResponse(order.getId()), localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CHANGE_STATUS_SUCCESS));
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
