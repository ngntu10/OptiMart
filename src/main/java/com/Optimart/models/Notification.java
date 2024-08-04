package com.Optimart.models;
        import java.util.List;
        import java.util.UUID;

        import jakarta.persistence.*;
        import lombok.*;
        import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "context", nullable = false)
    private String context;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "isRead", nullable = false)
    private int isRead =0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
