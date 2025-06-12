package site.pokemons.edpproject.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats", uniqueConstraints = @UniqueConstraint(columnNames = {"hall_id", "row_number", "seat_number"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private CinemaHall hall;

    public Seat(String seatNumber, CinemaHall hall) {
        this.seatNumber = seatNumber;
        this.hall = hall;
    }
}

