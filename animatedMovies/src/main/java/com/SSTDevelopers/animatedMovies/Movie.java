package db.project.animatedMovies;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    private ObjectId id;
    @Field("imdb_id")
    private String imdbId;
    private String title;
    private List<String> genres;
    private String overview;
    private String tagline;
    private Double revenue;
    private Double vote_average;
    private Integer runtime;
    private Date release_date;
    private Double popularity;
}
