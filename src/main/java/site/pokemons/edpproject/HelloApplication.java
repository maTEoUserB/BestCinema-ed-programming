package site.pokemons.edpproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import site.pokemons.edpproject.controller.*;
import site.pokemons.edpproject.model.CinemaHall;
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
        //Utworzenie widoków przy starcie
        Map<String, Parent> views = new HashMap<>();
        //Utworzenie kontrolerów
        Map<String, Object> controllers = new HashMap<>();

        //REPERTOIRE
        FXMLLoader fxmlRepertoireLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/repertoire-view.fxml"));
        RepertoireController repertoireController = new RepertoireController();
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

        //HALL
        FXMLLoader fxmlHallLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/cinema-hall-view.fxml"));
        CinemaHallController hallController = new CinemaHallController();
        fxmlHallLoader.setControllerFactory(type -> {
            if (type == CinemaHallController.class) {
                return hallController;
            } else {
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        Parent hallView = fxmlHallLoader.load();
        views.put("hall-view", hallView);
        controllers.put("hall-controller", hallController);

        //HALL
        FXMLLoader fxmlReservationLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/reservation-view.fxml"));
        ReservationController reservationController = new ReservationController();
        fxmlReservationLoader.setControllerFactory(type -> {
            if (type == ReservationController.class) {
                return reservationController;
            } else {
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        Parent reservationView = fxmlReservationLoader.load();
        views.put("reservation-view", reservationView);
        controllers.put("reservation-controller", reservationController);

        //ADMIN
        FXMLLoader fxmlAdminLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/admin-view.fxml"));
        AdminController adminController =  new AdminController();
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
        LoginController loginController = new LoginController();
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

        //USER PROFILE
        FXMLLoader fxmlProfileLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/user-profile-view.fxml"));
        UserProfileController profileController = new UserProfileController();
        fxmlProfileLoader.setControllerFactory(type -> {
            if (type == UserProfileController.class) {
                return profileController;
            } else {
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        Parent profileView = fxmlProfileLoader.load();
        views.put("profile-view", profileView);

        //WEJŚCIOWE OKNO REJESTRACJI
        FXMLLoader fxmlRegisterLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/register-view.fxml"));
        RegisterController regController = new RegisterController();
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
        profileController.setViews(views);
        reservationController.setViews(views);

        repertoireController.setControllers(controllers);

        Scene scene = new Scene(registerView);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        regController.setScene(scene);
        repertoireController.setScene(scene);
        adminController.setScene(scene);
        loginController.setScene(scene);
        profileController.setScene(scene);
        reservationController.setScene(scene);
        
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