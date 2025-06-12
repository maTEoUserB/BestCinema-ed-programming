package site.pokemons.edpproject.event;

import com.google.common.eventbus.EventBus;

public class AppEventBus {
    private static final EventBus instance = new EventBus();

    public static EventBus getInstance() {
        System.out.println("Returning AppEventBus instance: " + instance);
        return instance;
    }
}
