package com.SSTDevelopers.animatedMovies;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    String connectionString = "mongodb+srv://Sanika:Binghamton123@cluster0.0bqonqx.mongodb.net";

    // Create connection string object
    ConnectionString connString = new ConnectionString(connectionString);

    // Create MongoClientSettings
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connString)
            .build();

    // Create MongoClient
    MongoClient mongoClient = MongoClients.create(settings);

    // Get database and collection
    MongoDatabase database = mongoClient.getDatabase("MoviesAPI");
    MongoCollection<Document> movieCollection = database.getCollection("movies");

    //Query 2
    public default List<Object> getRevenueBasedOnGenre(){
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$match", new Document("tagline", new Document("$ne", null))
                .append("revenue", new Document("$ne", null))
                .append("genres", new Document("$ne", null))));
        pipeline.add(new Document("$group", new Document("_id", "$genres")
                .append("average_revenue", new Document("$avg",
                        new Document("$convert", new Document("input", "$revenue")
                                .append("to", "double")
                                .append("onError", 0L)
                                .append("onNull", 0L))))
                .append("tagline_count", new Document("$sum", 1L))));
        pipeline.add(new Document("$project", new Document("_id", 0L)
                .append("genres", "$_id")
                .append("average_revenue", 1L)
                .append("tagline_count", 1L)));
        pipeline.add(new Document("$sort", new Document("average_revenue", -1L)));

        return movieCollection.aggregate(pipeline).into(new ArrayList<>());
    }

    //Query 3
    public default List<Object> getRuntimeAnalysisPipeline() {
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$group",
                new Document("_id", "$release_date")
                        .append("averageRuntime",
                                new Document("$avg",
                                        new Document("$toDouble", "$runtime")))
                        .append("averageRating",
                                new Document("$avg",
                                        new Document("$toDouble", "$vote_average")))));
        pipeline.add(new Document("$sort",
                new Document("_id", 1L)));
        pipeline.add(new Document("$project",
                new Document("releaseYear",
                        new Document("$year",
                                new Document("date",
                                        new Document("$dateFromString",
                                                new Document("dateString", "$_id")))))
                        .append("averageRuntime", 1L)
                        .append("averageRating", 1L)));
        pipeline.add(new Document("$group",
                new Document("_id", "$releaseYear")
                        .append("averageRuntime",
                                new Document("$avg", "$averageRuntime"))
                        .append("averageRating",
                                new Document("$avg", "$averageRating"))));
        pipeline.add(new Document("$sort",
                new Document("_id", 1L)));

        return movieCollection.aggregate(pipeline).into(new ArrayList<>());
    }

    //Query 4
    public default List<Object> getStorytellingPatterns() {
        List<Document> pipeline = new ArrayList<>();

        // Stage 1: Group by genres, push overviews and taglines into arrays
        pipeline.add(new Document("$group",
                new Document("_id", "$genres")
                        .append("overviews",
                                new Document("$push", "$overview"))
                        .append("taglines",
                                new Document("$push", "$tagline"))));

        // Stage 2: Project genre, narrativeStructures, plotElements, and thematicMotifs
        pipeline.add(new Document("$project",
                new Document("genre", "$_id")
                        .append("narrativeStructures",
                                new Document("$reduce",
                                        new Document("input", "$overviews")
                                                .append("initialValue", "")
                                                .append("in",
                                                        new Document("$concatArrays", Arrays.asList("$$value", "$$this")))))
                        .append("plotElements",
                                new Document("$reduce",
                                        new Document("input", "$taglines")
                                                .append("initialValue", "")
                                                .append("in",
                                                        new Document("$concatArrays", Arrays.asList("$$value", "$$this")))))
                        .append("thematicMotifs",
                                new Document("$reduce",
                                        new Document("input", "$overviews")
                                                .append("initialValue", "")
                                                .append("in",
                                                        new Document("$concatArrays", Arrays.asList("$$value", "$$this")))))));

        // Stage 3: Output the result to "genre_analysis" collection
        pipeline.add(new Document("$out", "genre_analysis"));

        return movieCollection.aggregate(pipeline).into(new ArrayList<>());
    }

    Optional<Movie> findMovieByImdbId(String imdbId);
    Optional<Movie> findMovieByTitle(String title);
}
