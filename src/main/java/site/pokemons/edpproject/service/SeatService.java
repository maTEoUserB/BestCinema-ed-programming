package site.pokemons.edpproject.service;

import jakarta.persistence.EntityManager;
import site.pokemons.edpproject.model.CinemaHall;
import site.pokemons.edpproject.model.Seat;
import site.pokemons.edpproject.model.db.JpaPersistenceUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SeatService {
    public List<String> getOccupiedSeats(Long hallId){
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        CinemaHall hall = em.find(CinemaHall.class, hallId);
        List<Seat> seats = em.createQuery("" +
                        "SELECT s FROM Seat s " +
                        "WHERE s.hall = :hall " +
                        "AND s.occupied = true"
                        , Seat.class
                ).setParameter("hall",hall)
                .getResultStream().toList();

        return seats.stream().map(Seat::getSeatNumber).toList();
    }
}
