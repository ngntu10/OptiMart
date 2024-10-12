package com.Optimart.models;

import com.Optimart.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItemList;

    @ManyToOne
    @JoinColumn(name = "paymentType_id", nullable = false)
    private Paymenttype paymentMethod;

    @ManyToOne
    @JoinColumn(name = "deliveryType_id", nullable = false)
    private DeliveryType deliveryMethod;

    @Column(name = "itemsPrice" , nullable = false)
    private Long itemsPrice;

    @Column(name = "shippingPrice", nullable = false)
    private Long shippingPrice;

    @Column(name = "totalPrice", nullable = false)
    private Long totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "isPaid", nullable = false)
    private int isPaid = 0;

    @Column(name = "isDelivered", nullable = false)
    private int isDelivered = 0;

    @Column(name = "paidAt")
    private Date paidAt = new Date();

    @Column(name = "deliveryAt")
    private Date deliveryAt = new Date();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.WAIT_DELIVERY;

    @ManyToOne
    @JoinColumn(name = "shippingAddress_id")
    private ShippingAddress shippingAddress;
}
