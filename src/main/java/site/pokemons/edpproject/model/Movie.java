package site.pokemons.edpproject.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;


    @Column(unique = true)
    private long externalApiId;

    @OneToMany(mappedBy = "movie")
    private List<Screening> screenings;

    public Movie(String title, String overview, long id) {
        this.title = title;
        this.description = overview;
        this.externalApiId = id;
    }
}

