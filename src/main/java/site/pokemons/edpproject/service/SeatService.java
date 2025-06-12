package site.pokemons.edpproject.service;

import jakarta.persistence.EntityManager;
import site.pokemons.edpproject.model.CinemaHall;
import site.pokemons.edpproject.model.Seat;
import site.pokemons.edpproject.model.db.JpaPersistenceUnit;

import java.util.ArrayList;
import java.util.List;

public class SeatService {
    private static SeatService instance;

    private SeatService(){}

    public static synchronized SeatService getInstance() {
        if(instance == null) {
            instance = new SeatService();
        }
        return instance;
    }

    public List<String> getOccupiedSeats(Long screeningId) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        return em.createQuery("""
                        SELECT rs.seat.seatNumber
                        FROM ReservationSeat rs
                        WHERE rs.occupied = true
                          AND rs.reservation.screening.screeningId = :screeningId
                        """, String.class)
                .setParameter("screeningId", screeningId)
                .getResultList();
    }
}
