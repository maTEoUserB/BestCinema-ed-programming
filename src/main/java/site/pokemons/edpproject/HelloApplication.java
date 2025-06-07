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
        RepertoireController repertoireController = new RepertoireController(screeningService);
        fxmlRepertoireLoader.setControllerFactory(type -> {
            if (type == RepertoireController.class) {
                return repertoireController;
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
        FXMLLoader fxmlAdminLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/admin-view.fxml"));
        AdminController adminController =  new AdminController(tmdbApiService, screeningService);
        fxmlAdminLoader.setControllerFactory(type -> {
            if (type == AdminController.class) {
                return adminController;
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
        LoginController loginController = new LoginController(userService);
        fxmlLoginLoader.setControllerFactory(type -> {
            if (type == LoginController.class) {
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
        FXMLLoader fxmlRegisterLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/register-view.fxml"));
        RegisterController regController = new RegisterController(userService);
        fxmlRegisterLoader.setControllerFactory(type -> {
                if(type == RegisterController.class){
                    return regController;
                }else if(type == LoginController.class){
                    return loginController;
                }else{
                    try{
                        return type.getDeclaredConstructor().newInstance();
                    }catch (Exception e){
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
        });
        Parent registerView = fxmlRegisterLoader.load();
        views.put("register-view", registerView);

        regController.setViews(views);
        adminController.setViews(views);
        loginController.setViews(views);
        repertoireController.setViews(views);

        Scene scene = new Scene(registerView);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        regController.setScene(scene);
        repertoireController.setScene(scene);
        adminController.setScene(scene);
        loginController.setScene(scene);

//        stage.setMaximized(true);
//        stage.setResizable(true);
        stage.setWidth(900);
        stage.setHeight(570);
        stage.setTitle("BestCinema");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}