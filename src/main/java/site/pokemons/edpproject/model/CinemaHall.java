package site.pokemons.edpproject.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cinema_halls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hallId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer totalSeats;

    @OneToMany(mappedBy = "hall")
    private List<Seat> seats;

    @OneToMany(mappedBy = "hall")
    private List<Screening> screenings;
}

