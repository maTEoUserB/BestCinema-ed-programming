package site.pokemons.edpproject.service.serviceSingleton;

import site.pokemons.edpproject.service.TmdbApiService;

import java.io.IOException;

public class TmdbApiServiceSingleton {
    private static TmdbApiService instance;

    private TmdbApiServiceSingleton() {}

    public static synchronized TmdbApiService getInstance() throws IOException, InterruptedException {
        if (instance == null) {
            return new TmdbApiService();
        }
        return instance;
    }
}
