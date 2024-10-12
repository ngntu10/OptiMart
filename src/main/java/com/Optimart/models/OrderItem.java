package com.Optimart.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_item")
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "discount", nullable = false)
    private int discount;

    @Column(name = "slug", nullable = false)
    private String slug;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;
}
