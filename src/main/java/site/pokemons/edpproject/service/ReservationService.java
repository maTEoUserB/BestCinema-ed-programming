package site.pokemons.edpproject.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import site.pokemons.edpproject.model.*;
import site.pokemons.edpproject.model.db.JpaPersistenceUnit;
import site.pokemons.edpproject.session.SessionContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationService {
    private static ReservationService instance;

    private ReservationService(){}

    public static synchronized ReservationService getInstance() {
        if (instance == null) {
            return new ReservationService();
        }
        return instance;
    }

    public Reservation addReservation(Long screeningId) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        User userProxy = em.getReference(User.class, SessionContext.getLoggedInUserId());
        Screening screeningProxy = em.getReference(Screening.class, screeningId);

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Reservation reservation = Reservation.builder()
                    .user(userProxy)
                    .screening(screeningProxy)
                    .reservationTime(LocalDateTime.now())
                    .status("pending")
                    .build();

            em.persist(reservation);
            tx.commit();
            return reservation;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

    public boolean deleteReservation(Long reservationId) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("""
                            DELETE FROM ReservationSeat rs
                            WHERE rs.reservation.reservationId = :reservationId
                            """)
                    .setParameter("reservationId", reservationId)
                    .executeUpdate();

            em.remove(em.getReference(Reservation.class, reservationId));
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return false;
    }

    public Long addReservationSeat(String seatNumber, String hallId, Long screeningId, Long reservationId) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        List<ReservationSeat> reservationSeats = em.createQuery(
                        "SELECT s FROM ReservationSeat s " +
                                "WHERE s.seat.seatNumber = :seatNr " +
                                "AND s.reservation.screening.screeningId =: screeningId " +
                                "AND s.occupied = true",
                        ReservationSeat.class
                )
                .setParameter("seatNr", seatNumber)
                .setParameter("screeningId", screeningId)
                .getResultList();

        if (!reservationSeats.isEmpty()) {
            return null;
        }

        Reservation reservationProxy = em.getReference(Reservation.class, reservationId);
        Seat seat = em.createQuery(
                        "SELECT s FROM Seat s WHERE s.seatNumber = :seatNr AND s.hall.hallId = :hallId",
                        Seat.class
                )
                .setParameter("seatNr", seatNumber)
                .setParameter("hallId", hallId)
                .getSingleResult();

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            ReservationSeat newReservationSeat = ReservationSeat.builder()
                    .reservation(reservationProxy)
                    .seat(seat)
                    .occupied(true)
                    .build();

            em.persist(newReservationSeat);
            tx.commit();
            return newReservationSeat.getReservationSeatId();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

    public void updateReservationSeat(Long reservationSeatId, boolean isReduced) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        ReservationSeat reservationSeat = em.createQuery(
                        "SELECT s FROM ReservationSeat s " +
                                "WHERE s.reservationSeatId = :id ",
                        ReservationSeat.class
                )
                .setParameter("id", reservationSeatId)
                .getSingleResult();

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            reservationSeat.setReduced(isReduced);

            em.persist(reservationSeat);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public String getReservationInformation(Long reservationId) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        String title = em.createQuery("SELECT r.screening.movie.title " +
                                "FROM Reservation r " +
                                "WHERE r.reservationId = :resId"
                        , String.class)
                .setParameter("resId", reservationId)
                .getSingleResult();

        Long hallId = em.createQuery("SELECT r.screening.hall.hallId " +
                                "FROM Reservation r " +
                                "WHERE r.reservationId = :resId"
                        , Long.class)
                .setParameter("resId", reservationId)
                .getSingleResult();

        LocalDateTime startTime = em.createQuery("SELECT r.screening.startTime " +
                                "FROM Reservation r " +
                                "WHERE r.reservationId = :resId"
                        , LocalDateTime.class)
                .setParameter("resId", reservationId)
                .getSingleResult();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String time = startTime.format(formatter);

        List<Object[]> resultList = em.createQuery("""
                            SELECT rs.seat.seatNumber, rs.occupied
                            FROM ReservationSeat rs
                            WHERE rs.reservation.reservationId = :resId
                        """, Object[].class)
                .setParameter("resId", reservationId)
                .getResultList();

        Map<String, Boolean> seats = resultList.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Boolean) row[1]
                ));

        Map<String, String> markedSeats = new HashMap<>();
        seats.forEach((key, value) -> {
                    if(value) markedSeats.put(key, "Bilet ulgowy.");
                    else markedSeats.put(key, "Bilet normalny.");
                });

        String resultSeats = markedSeats.entrySet().stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining("\n"));

        return "Dokonałeś rezerwacji na film " + title + ".\nData seansu: " + time + ".\nSala: " + hallId + ".\nMiejsca:\n " + resultSeats + ".\n\nDo zobaczenia w naszym kinie! :)";
    }
}
