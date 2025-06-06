package site.pokemons.edpproject.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import site.pokemons.edpproject.model.Movie;
import site.pokemons.edpproject.model.db.JpaPersistenceUnit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RepertoireController {

    public RepertoireController() {}

    @FXML
    public final void onSelectionChanged(Event event){
        EntityManager em = JpaPersistenceUnit.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        Object source = event.getSource();
        Tab tab = null;
        if(!(source instanceof Tab)){
            return;
        }
        tab = (Tab) source;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(tab.getText(), formatter);
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = startDate.plusDays(1).atStartOfDay();

        List<Movie> movies = List.of();

        try{
//            TypedQuery<Movie> query = em.createQuery("SELECT m" +
//                    " FROM Movie m" +
//                    " WHERE m.date >= :startTime" +
//                    " AND m.date < :endTime", Movie.class);
//            query.setParameter("startTime", startTime);
//            query.setParameter("endTime", endTime);

            TypedQuery<Movie> query = em.createQuery("SELECT m" +
                    " FROM Movie m", Movie.class);

            movies = query.getResultStream().toList();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(em.isOpen()) em.close();
        }

        VBox vBox = new VBox();
        for(Movie m : movies) {
            VBox movieBox = new VBox();
            movieBox.setSpacing(10);
//            movieBox.getChildren().addAll(new Label(m.getTitle()), new Label(m.getDescription()),
//                    new Label(m.getDate().toLocalTime().toString()), new Label(""+m.getHall()+""));

            vBox.getChildren().add(movieBox);
        }

        tab.setContent(vBox);
    }
}
