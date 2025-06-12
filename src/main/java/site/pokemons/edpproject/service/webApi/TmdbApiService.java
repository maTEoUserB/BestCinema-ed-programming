package site.pokemons.edpproject.service.webApi;


import com.fasterxml.jackson.databind.ObjectMapper;
import site.pokemons.edpproject.model.tmdbApiDto.NowPlayingResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TmdbApiService {
    private static TmdbApiService instance;

    private TmdbApiService() {
    }

    public static synchronized TmdbApiService getInstance() throws IOException, InterruptedException {
        if (instance == null) {
            instance =  new TmdbApiService();
        }
        return instance;
    }

    public NowPlayingResponse getMovieList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/now_playing?api_key=" + System.getenv("TMBD_API_KEY") + "&include_adult=false&language=pl-PL&page=1"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new ObjectMapper().readValue(response.body(), NowPlayingResponse.class);
    }
}
