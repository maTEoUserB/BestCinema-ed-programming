package site.pokemons.edpproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import site.pokemons.edpproject.controller.LoginController;
import site.pokemons.edpproject.controller.RegisterController;
import site.pokemons.edpproject.service.UserService;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/register-view.fxml"));

        UserService userService = new UserService();
        fxmlLoader.setControllerFactory(type -> {
                if(type == RegisterController.class){
                    RegisterController regController = new RegisterController(userService);
                    regController.setPrimaryStage(stage);
                    return regController;
                }else if(type == LoginController.class){
                    return new LoginController(userService);
                }else{
                    try{
                        return type.getDeclaredConstructor().newInstance();
                    }catch (Exception e){
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
        });

        Scene scene = new Scene(fxmlLoader.load(), 500, 300);
        stage.setTitle("BestCinema");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}