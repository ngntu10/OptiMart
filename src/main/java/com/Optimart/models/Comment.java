package com.Optimart.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Comment> replies;

}
