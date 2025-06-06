package site.pokemons.edpproject.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import site.pokemons.edpproject.model.tmdbApiDto.MovieDTO;
import site.pokemons.edpproject.model.tmdbApiDto.NowPlayingResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TmdbApiService {
    public TmdbApiService() throws IOException, InterruptedException {
    }

    public NowPlayingResponse getMovieList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/now_playing?api_key=f381cf50b9371d27bc42784561474705&include_adult=false&language=pl-PL&page=1"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        NowPlayingResponse nowPlayingResponse = new ObjectMapper().readValue(response.body(), NowPlayingResponse.class);

        return nowPlayingResponse;
    }
}
