package site.pokemons.edpproject.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.scene.control.Alert;
import site.pokemons.edpproject.model.Movie;
import site.pokemons.edpproject.model.db.JpaPersistenceUnit;
import site.pokemons.edpproject.model.tmdbApiDto.MovieDTO;


public class MovieService {
    public long saveMovie(MovieDTO movieDTO) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        Movie movie = em.createQuery("SELECT m FROM Movie m WHERE m.title = :title", Movie.class)
                .setParameter("title", movieDTO.getTitle()).getResultStream().findFirst().orElse(null);
        if(movie != null)  return movie.getMovieId();

        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();

            Movie newMovie = new Movie(movieDTO.getTitle(), movieDTO.getOverview(), movieDTO.getId());
            em.persist(newMovie);

            showAlert("Film zapisano do bazy!", Alert.AlertType.INFORMATION);
            return newMovie.getMovieId();
        } catch (Exception e){
            if(tx.isActive()) tx.rollback();
            e.printStackTrace();
            showAlert("Nie udało się zapisać filmu w bazie.", Alert.AlertType.ERROR);
        } finally {
            if(tx.isActive()) tx.commit();
            em.close();
        }
        return 0;
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
