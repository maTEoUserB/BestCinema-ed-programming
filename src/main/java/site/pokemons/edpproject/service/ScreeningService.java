package site.pokemons.edpproject.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.scene.control.Alert;
import site.pokemons.edpproject.model.CinemaHall;
import site.pokemons.edpproject.model.Movie;
import site.pokemons.edpproject.model.Screening;
import site.pokemons.edpproject.model.db.JpaPersistenceUnit;
import site.pokemons.edpproject.model.dbDto.ScreeningDTO;
import site.pokemons.edpproject.model.tmdbApiDto.MovieDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ScreeningService {
    private final MovieService movieService;

    public ScreeningService(MovieService movieService) {
        this.movieService = movieService;
    }

    public Screening saveScreening(MovieDTO movie, double price, int hallId, LocalDate date, int hour, int minute) {
        long movieId = movieService.saveMovie(movie);

        EntityManager em = JpaPersistenceUnit.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        Movie movieProxy = em.getReference(Movie.class, movieId);
        CinemaHall hallProxy = em.getReference(CinemaHall.class, hallId);
        try{
            tx.begin();


            Screening screening = Screening.builder()
                    .movie(movieProxy)
                    .hall(hallProxy)
                    .startTime(LocalDateTime.of(date, LocalTime.of(hour, minute)))
                    .price(price)
                    .build();
            em.persist(screening);

            showAlert("Ekranizację zapisano do bazy!", Alert.AlertType.INFORMATION);
            return screening;
        } catch (Exception e){
            if(tx.isActive()) tx.rollback();
            e.printStackTrace();
            showAlert("Nie udało się zapisać ekranizacji w bazie.", Alert.AlertType.ERROR);
        } finally {
            if(tx.isActive()) tx.commit();
            em.close();
        }
        return null;
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public List<ScreeningDTO> findScreenings(LocalDate date) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Screening> screenings = em.createQuery(
                     "SELECT s FROM Screening s " +
                        "JOIN FETCH s.movie " +
                        "WHERE s.startTime BETWEEN :start AND :end",
                        Screening.class)
                .setParameter("start", startOfDay)
                .setParameter("end", endOfDay)
                .getResultList();

        return screenings.stream().map(s -> {
            ScreeningDTO screeningDTO = new ScreeningDTO();
            screeningDTO.setScreeningId(s.getScreeningId());
            screeningDTO.setPrice(s.getPrice());
            screeningDTO.setHallId(s.getHall().getHallId());
            screeningDTO.setStartTime(s.getStartTime());
            screeningDTO.setTitle(s.getMovie().getTitle());
            screeningDTO.setDescription(s.getMovie().getDescription());
            screeningDTO.setImageUrl(s.getMovie().getImageUrl());
            return screeningDTO;
        }).collect(Collectors.toList());
    }
}
