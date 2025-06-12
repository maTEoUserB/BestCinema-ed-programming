package site.pokemons.edpproject.controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class ViewManager {
    private static ViewManager instance;

    @Getter
    @Setter
    private Scene scene;
    private final Map<String, Parent> views = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>();

    private ViewManager() {}

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    public void addView(String name, Parent view) {
        views.put(name, view);
    }

    public void addController(String name, Object controller) {
        controllers.put(name, controller);
    }

    public void switchTo(String viewName) {
        Parent view = views.get(viewName);
        if (view != null && scene != null) {
            scene.setRoot(view);
        } else {
            throw new IllegalStateException("Nie znaleziono widoku lub sceny: " + viewName);
        }
    }

    public <T> T getController(String name, Class<T> type) {
        Object controller = controllers.get(name);
        return type.cast(controller);
    }

    public Parent getView(String name) {
        return views.get(name);
    }
}
