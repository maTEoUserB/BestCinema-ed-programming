package site.pokemons.edpproject.service.serviceSingleton;

import site.pokemons.edpproject.service.ScreeningService;

public class ScreeningServiceSingleton {
    private static ScreeningService instance;

    private ScreeningServiceSingleton() {}

    public static synchronized ScreeningService getInstance() {
        if (instance == null) {
            return new ScreeningService();
        }
        return instance;
    }
}
