package site.pokemons.edpproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    public User(String username, String hashedPassword, String email, String name, String surname, LocalDateTime now, String role) {
        this.username = username;
        this.passwordHash = hashedPassword;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.createdAt = now;
        this.role = role;
    }
}
