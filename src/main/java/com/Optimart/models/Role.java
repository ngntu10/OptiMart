package com.Optimart.models;
import java.util.UUID;

import com.Optimart.enums.RoleNameEnum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private RoleNameEnum name;

    @Column(name = "permission")
    private String permission;

    @OneToOne(mappedBy = "role")
    private User user;


}
