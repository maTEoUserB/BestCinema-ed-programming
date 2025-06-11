package site.pokemons.edpproject.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.scene.control.Alert;
import site.pokemons.edpproject.model.Movie;
import site.pokemons.edpproject.model.db.JpaPersistenceUnit;
import site.pokemons.edpproject.model.tmdbApiDto.MovieDTO;


public class MovieService {
    private static MovieService instance;

    private MovieService(){}

    public static synchronized MovieService getInstance() {
        if (instance == null) {
            instance = new MovieService();
        }
        return instance;
    }

    public long saveMovie(MovieDTO movieDTO) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        Movie movie = em.createQuery("SELECT m FROM Movie m WHERE m.title = :title", Movie.class)
                .setParameter("title", movieDTO.getTitle()).getResultStream().findFirst().orElse(null);
        if(movie != null)  return movie.getMovieId();

        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();

            String imageUrl = "https://image.tmdb.org/t/p/w400" + movieDTO.getPosterPath();
            Movie newMovie = new Movie(movieDTO.getTitle(), movieDTO.getOverview(), imageUrl, movieDTO.getId());
            em.persist(newMovie);

            showAlert("Film zapisano do bazy!", Alert.AlertType.INFORMATION);
            tx.commit();
            return newMovie.getMovieId();
        } catch (Exception e){
            if(tx.isActive()) tx.rollback();
            e.printStackTrace();
            showAlert("Nie udało się zapisać filmu w bazie.", Alert.AlertType.ERROR);
        } finally {
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
