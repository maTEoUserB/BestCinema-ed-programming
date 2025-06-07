package site.pokemons.edpproject.model.dbDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScreeningDTO {
    private long screeningId;
    private Double price;
    private long hallId;
    private LocalDateTime startTime;
    private String title;
    private String description;
    private String imageUrl;
}
