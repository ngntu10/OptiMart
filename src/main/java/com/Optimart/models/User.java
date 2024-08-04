package com.Optimart.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "status")
    private int status = 1;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "userType")
    private int userType = 3;

    @Column(name = "resetToken")
    private String resetToken;

    @Column(name = "resetTokenExpiration")
    private Date resetTokenExpiration;

    @Column(name = "deviceToken")
    private String deviceToken;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToMany
    @JoinTable(
            name = "user_has_role",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
    )
    List<Role> roleList;

    @ManyToMany
    @JoinTable(
            name = "user_like_product",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "product_id", nullable = false)
    )
    List<Product> likeProductList;


    @ManyToOne
    @JoinColumn(name = "uniqueviews")
    private Product product;

    @ManyToMany
    @JoinTable(
            name = "user_view_product",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "product_id", nullable = false)
    )
    List<Product> viewedProductList;

    @OneToMany(mappedBy = "user")
    private List<userShippingAddress> userShippingAddressList;

    @OneToMany(mappedBy = "user")
    private List<Review> reviewList;

    @OneToMany(mappedBy = "user")
    private List<Notification> notificationList;

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList;
}
