package site.pokemons.edpproject.service.serviceSingleton;

import site.pokemons.edpproject.service.SeatService;

/**
 * Singleton for SeatService object.
 */
public class SeatServiceSingleton {
    private static SeatService instance;

    private SeatServiceSingleton(){}

    public static synchronized SeatService getInstance() {
        if(instance == null) {
            instance = new SeatService();
        }
        return instance;
    }
}
