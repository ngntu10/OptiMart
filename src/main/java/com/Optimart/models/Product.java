package com.Optimart.models;

import com.Optimart.models.Listener.ProductListener;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@EntityListeners(ProductListener.class)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "image")
    private String image;

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "countInStock",nullable = false)
    private int countInStock;

    @Column(name = "description",  length = 100000)
    private String description;

    @Column(name = "discount")
    private int discount;

    @Column(name = "discountStartDate")
    private Date discountStartDate;

    @Column(name = "discountEndDate")
    private Date discountEndDate;

    @Column(name = "sold")
    private int sold;

    @Column(name = "totalLikes")
    private int totalLikes =0;

    @Column(name =  "status")
    private int status = 0;

    @Column(name = "views")
    private int views = 0;

    @OneToMany(mappedBy = "product")
    private List<User> userList;

//    @ManyToMany
//    @JoinTable(
//            name = "locationProduct",
//            joinColumns = @JoinColumn(name = "product_id"),
//            inverseJoinColumns = @JoinColumn(name = "city_id")
//    )
//    private List<City> cityList;

    @ManyToMany(mappedBy = "likeProductList")
    @JsonIgnore
    private List<User> userLikedList;

    @JsonIgnore
    @ManyToMany(mappedBy = "viewedProductList")
    private Set<User> userViewedList;

//    @ManyToMany
//    @JoinTable(
//            name = "productTypes",
//            joinColumns = @JoinColumn(name = "product_id"),
//            inverseJoinColumns = @JoinColumn(name = "productType_id")
//    )
//    private List<ProductType> productTypeList;

    @ManyToOne
    @JoinColumn(name = "productTypeId")
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "cityId")
    private City city;

    @OneToMany(mappedBy = "product")
    private List<Review> reviewList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;
}
