package com.Optimart.dto.Order;

import com.Optimart.dto.OrderItem.OrderItemsDTO;
import com.Optimart.dto.ShippingAddress.ShippingAddressDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {
  private String address;
  private String city;
  private String deliveryMethod;
  private String fullName;
  private Long itemsPrice;
  private List<OrderItemsDTO> orderItems;
  private String paymentMethod;
  private String phone;
  private Long shippingPrice;
  private Long totalPrice;
  @JsonProperty("user")
  private String userId;
}
