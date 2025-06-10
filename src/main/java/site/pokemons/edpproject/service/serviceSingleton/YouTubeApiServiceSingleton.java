package site.pokemons.edpproject.service.serviceSingleton;

import site.pokemons.edpproject.service.webApi.YouTubeApiService;

public class YouTubeApiServiceSingleton {
    private static YouTubeApiService instance;

    private YouTubeApiServiceSingleton() {}

    public static YouTubeApiService getInstance() {
        if (instance == null) {
            instance = new YouTubeApiService();
        }
        return instance;
    }
}
