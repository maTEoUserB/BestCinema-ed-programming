package site.pokemons.edpproject.service.serviceSingleton;

import site.pokemons.edpproject.service.ReservationService;

/**
 * Singleton for ReservationService object.
 */
public class ReservationServiceSingleton {
    private static ReservationService instance;

    private ReservationServiceSingleton(){}

    public static synchronized ReservationService getInstance() {
        if (instance == null) {
            return new ReservationService();
        }
        return instance;
    }
}
