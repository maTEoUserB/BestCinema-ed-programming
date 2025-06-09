package site.pokemons.edpproject.service.serviceSingleton;

import site.pokemons.edpproject.service.MovieService;

public class MovieServiceSingleton {
    private static MovieService instance;

    private MovieServiceSingleton(){}

    public static synchronized MovieService getInstance() {
        if (instance == null) {
            instance = new MovieService();
        }
        return instance;
    }
}
