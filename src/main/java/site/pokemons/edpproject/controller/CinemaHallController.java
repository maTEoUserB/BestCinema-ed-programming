package site.pokemons.edpproject.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import site.pokemons.edpproject.controller.component.PersonRow;
import site.pokemons.edpproject.model.*;
import site.pokemons.edpproject.model.db.JpaPersistenceUnit;
import site.pokemons.edpproject.service.serviceSingleton.SeatServiceSingleton;
import site.pokemons.edpproject.session.SessionContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CinemaHallController {
    @FXML private AnchorPane rootPane;
    @FXML private Label hallNumber;
    @FXML
    private VBox personList;
    private List<PersonRow> personRows = new ArrayList<>();
    private Reservation reservation;
    @Setter
    private Long screeningId;

    public void setHallNumber(long hallNumber) {
        this.hallNumber.setText(String.valueOf(hallNumber));
    }

    public void setScreeningId(Long screeningId) {
        this.screeningId = screeningId;
        markOccupiedSeats();
        reservation = addReservation();
    }

//    @FXML
//    public void initialize() {
//        markOccupiedSeats();
//
//        reservation = addReservation();
//    }

    //przenieś do serwisu
    private Reservation addReservation() {
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
            return reservation;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            if (tx.isActive()) tx.commit();
            em.close();
        }
        return null;
    }

    @FXML
    public void chooseSeat(MouseEvent mouseEvent) {
        Button clickedSeat = (Button) mouseEvent.getSource();
        clickedSeat.setStyle("-fx-background-color: red;");

        Long id = addReservationSeat(clickedSeat.getId());
        //na końcu updateuj ReservationSeats przy zatwierdzeniu rezerwacji
        if(id != null) addPerson(id, clickedSeat.getId());
    }

    @FXML
    public void toNextStep(MouseEvent mouseEvent) {
        for(PersonRow personRow : personRows) {
            updateReservationSeat(personRow.getReservationSeatId(), personRow.getIsReduced().isSelected());
        }
    }

    private void updateReservationSeat(Long reservationSeatId, boolean isReduced) {
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
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            if (tx.isActive()) tx.commit();
            em.close();
        }
    }

    @FXML
    public void goBack(MouseEvent mouseEvent) {

    }

    //przenieś do serwisu
    private Long addReservationSeat(String seatNumber) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        List<ReservationSeat> reservationSeats = em.createQuery(
                        "SELECT s FROM ReservationSeat s " +
                                "WHERE s.seat.seatNumber = :seatNr " +
                                "AND s.seat.hall.hallId = :hallId " +
                                "AND s.occupied = true",
                        ReservationSeat.class
                )
                .setParameter("seatNr", seatNumber)
                .setParameter("hallId", hallNumber.getText())
                .getResultList();

        if(!reservationSeats.isEmpty()) {
            showAlert("Miejsce zajęte.", Alert.AlertType.ERROR);
            return null;
        }

        Reservation reservationProxy = em.getReference(Reservation.class, reservation.getReservationId());
        Seat seat = em.createQuery(
                        "SELECT s FROM Seat s WHERE s.seatNumber = :seatNr AND s.hall.hallId = :hallId",
                        Seat.class
                )
                .setParameter("seatNr", seatNumber)
                .setParameter("hallId", hallNumber.getText())
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
            return newReservationSeat.getReservationSeatId();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            if (tx.isActive()) tx.commit();
            em.close();
        }
        return null;
    }

    private void addPerson(Long id, String seatNumber) {
        PersonRow personRow = new PersonRow(id, seatNumber);
        personRows.add(personRow);
        personList.getChildren().add(personRow.getLayout());
    }

    private void markOccupiedSeats() {
        List<String> occupiedSeats = SeatServiceSingleton.getInstance().getOccupiedSeats(Long.parseLong(hallNumber.getText()));
        for (String seatId : occupiedSeats) {
            Node node = rootPane.lookup("#" + seatId);
            if (node instanceof Button) {
                Button seatButton = (Button) node;
                seatButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
                seatButton.setDisable(true);
            }
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
