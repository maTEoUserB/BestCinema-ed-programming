package site.pokemons.edpproject.service.webApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import site.pokemons.edpproject.model.tmdbApiDto.NowPlayingResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class YouTubeApiService {
    private static YouTubeApiService instance;

    private YouTubeApiService() {
    }

    public static YouTubeApiService getInstance() {
        if (instance == null) {
            instance = new YouTubeApiService();
        }
        return instance;
    }

    public String getMovieTrailerLink(String title) throws IOException, InterruptedException {
        String encodedTitle = URLEncoder.encode(title + " ", StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + encodedTitle + "trailer&type=video&key=" + System.getenv("YT_API_KEY")))
                .build();

        HttpResponse<String> jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        return getVideoId(jsonResponse);
    }

    private String getVideoId(HttpResponse<String> jsonResponse) {
        JSONObject response = new JSONObject(jsonResponse.body());
        JSONArray items = response.getJSONArray("items");
        JSONObject item = items.getJSONObject(0);
        JSONObject idObject = item.getJSONObject("id");

        return idObject.getString("videoId");
    }
}
