package site.pokemons.edpproject.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_seats", uniqueConstraints = @UniqueConstraint(columnNames = {"reservation_id", "seat_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationSeatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name="occupied", nullable = false)
    private boolean occupied;

    @Column(name="is_reduced", nullable = false)
    private boolean isReduced;
}

