package site.pokemons.edpproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import site.pokemons.edpproject.controller.AdminController;
import site.pokemons.edpproject.controller.LoginController;
import site.pokemons.edpproject.controller.RegisterController;
import site.pokemons.edpproject.controller.RepertoireController;
import site.pokemons.edpproject.model.tmdbApiDto.MovieDTO;
import site.pokemons.edpproject.model.tmdbApiDto.NowPlayingResponse;
import site.pokemons.edpproject.service.MovieService;
import site.pokemons.edpproject.service.ScreeningService;
import site.pokemons.edpproject.service.TmdbApiService;
import site.pokemons.edpproject.service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        UserService userService = new UserService();
        MovieService movieService = new MovieService();
        ScreeningService screeningService = new ScreeningService(movieService);
        TmdbApiService tmdbApiService = new TmdbApiService();

        //Utworzenie widoków przy starcie
        Map<String, Parent> views = new HashMap<>();

        //REPERTOIRE
        FXMLLoader fxmlRepertoireLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/repertoire-view.fxml"));
        fxmlRepertoireLoader.setControllerFactory(type -> {
            if (type == RepertoireController.class) {
                return new RepertoireController();
            } else {
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        Parent repertoireView = fxmlRepertoireLoader.load();
        views.put("repertoire-view", repertoireView);

        //ADMIN
//        TmdbApiService tmdbApiService = new TmdbApiService();
//        NowPlayingResponse movies = tmdbApiService.getMovieList();
        FXMLLoader fxmlAdminLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/admin-view.fxml"));
        fxmlAdminLoader.setControllerFactory(type -> {
            if (type == AdminController.class) {
                return new AdminController(tmdbApiService, screeningService);
            } else {
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        Parent adminView = fxmlAdminLoader.load();
        views.put("admin-view", adminView);

        //LOGIN
        FXMLLoader fxmlLoginLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/login-view.fxml"));
        fxmlLoginLoader.setControllerFactory(type -> {
            if (type == LoginController.class) {
                LoginController loginController = new LoginController(userService, views);
                loginController.setPrimaryStage(stage);
                return loginController;
            } else {
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        Parent loginView = fxmlLoginLoader.load();
        views.put("login-view", loginView);



        //WEJŚCIOWE OKNO REJESTRACJI
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/register-view.fxml"));
        fxmlLoader.setControllerFactory(type -> {
                if(type == RegisterController.class){
                    RegisterController regController = new RegisterController(userService, views);
                    regController.setPrimaryStage(stage);
                    return regController;
                }else if(type == LoginController.class){
                    return new LoginController(userService, views);
                }else{
                    try{
                        return type.getDeclaredConstructor().newInstance();
                    }catch (Exception e){
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
        });

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("BestCinema");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}