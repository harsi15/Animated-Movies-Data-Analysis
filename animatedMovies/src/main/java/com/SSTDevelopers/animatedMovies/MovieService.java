package db.project.animatedMovies;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    public List<?> findCommonThemes() {
//        MatchOperation matchStage = Aggregation.match(Criteria.where("tagline").exists(true));
//        GroupOperation groupStage = Aggregation.group("genre").push("tagline").as("taglines");
//        Aggregation aggregation = Aggregation.newAggregation(matchStage, groupStage);
//        AggregationResults<?> result = mongoTemplate.aggregate(aggregation, "movies", Object.class);
//        return result.getMappedResults();

//    }
    @Autowired
    private MovieRepository movieRepository;
    public List<Movie> allMovies(){
        return movieRepository.findAll();
    }
    public Optional<Movie> singleMovie(String imdbId)
    {
        return movieRepository.findMovieByImdbId(imdbId);
    }

}
