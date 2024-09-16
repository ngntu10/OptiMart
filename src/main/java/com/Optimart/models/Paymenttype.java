package com.Optimart.models;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import com.Optimart.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_types")
public class Paymenttype extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "paymentMethod")
    List<Order> orders;

//    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private String paymentType;




}
