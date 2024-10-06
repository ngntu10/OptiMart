package com.Optimart.models;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cities")
public class City extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "city")
    List<ShippingAddress> shippingAddresses;

    @JsonIgnore
    @OneToMany(mappedBy = "city")
    List<User> userList;

    @JsonIgnore
    @OneToMany(mappedBy = "city")
    List<Product> productList;

}
