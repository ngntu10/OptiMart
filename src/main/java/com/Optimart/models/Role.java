package com.Optimart.models;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "permission")
    private String permission;

//    @ManyToMany(mappedBy = "roleList")
//    private List<User> userList;
}
