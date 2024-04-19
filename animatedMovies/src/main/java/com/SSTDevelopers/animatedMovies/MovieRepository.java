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
import java.util.regex.Pattern;

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

    //Query 1
    public default List<Object> getRevenueBasedOnGenre() {
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$match", new Document("tagline", new Document("$ne", null))              //match
                .append("revenue", new Document("$ne", null))
                .append("budget", new Document("$ne", null))
                .append("genres", new Document("$ne", null))));
        pipeline.add(new Document("$group", new Document("_id", "$genres")                                  //group
                .append("average_revenue", new Document("$avg",
                        new Document("$convert", new Document("input", "$revenue")
                                .append("to", "double")
                                .append("onError", 0L)
                                .append("onNull", 0L))))
                .append("average_budget", new Document("$avg",
                        new Document("$convert", new Document("input", "$budget")
                                .append("to", "double")
                                .append("onError", 0L)
                                .append("onNull", 0L))))
                .append("tagline_count", new Document("$sum", 1L))));
        pipeline.add(new Document("$project", new Document("_id", 0L)                                       //Project
                .append("genres", "$_id")
                .append("average_revenue", 1L)
                .append("average_budget", 1L)
                .append("tagline_count", 1L)));
        pipeline.add(new Document("$sort", new Document("average_revenue", -1L)));                          //Sort

        return movieCollection.aggregate(pipeline).into(new ArrayList<>());
    }


    //Query 2
    public default List<Object> getRuntimeAndTaglineAnalysisPipeline() {
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$group",
                new Document("_id", "$release_date")
                        .append("averageRuntime",
                                new Document("$avg",
                                        new Document("$toDouble", "$runtime")))
                        .append("averageRating",
                                new Document("$avg",
                                        new Document("$toDouble", "$vote_average")))
                        .append("taglines", new Document("$push", "$tagline"))));
        pipeline.add(new Document("$sort",
                new Document("_id", 1L)));
        pipeline.add(new Document("$project",
                new Document("releaseYear",
                        new Document("$year",
                                new Document("date",
                                        new Document("$dateFromString",
                                                new Document("dateString", "$_id")))))
                        .append("averageRuntime", 1L)
                        .append("averageRating", 1L)
                        .append("taglines", 1L)));
        pipeline.add(new Document("$unwind", "$taglines"));
        pipeline.add(new Document("$group",
                new Document("_id", "$releaseYear")
                        .append("averageRuntime",
                                new Document("$avg", "$averageRuntime"))
                        .append("averageRating",
                                new Document("$avg", "$averageRating"))
                        .append("taglines", new Document("$push", "$taglines"))));
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
                        .append("narrativeStructures", "$overviews")
                        .append("plotElements", "$taglines")
                        .append("thematicMotifs", "$overviews")));

        // Stage 3: Output the result to "genre_analysis" collection
        pipeline.add(new Document("$out", "genre_analysis"));

        return movieCollection.aggregate(pipeline).into(new ArrayList<>());
    }

    //Basic filter for Genre
    public default List<Object> getFilterBasedOnGenre(String genre) {
        List<Document> pipeline = new ArrayList<>();

        // Stage 1: Match documents where the "genres" field matches the provided genre
        pipeline.add(new Document("$match",
                new Document("genres",
                        new Document("$regex", genre)
                                .append("$options", "i"))));

        // Now, perform the aggregation using the modified pipeline
        return movieCollection.aggregate(pipeline).into(new ArrayList<>());
    }

    //Basic filter based on production company
    public default List<Object> getFilterBasedOnProduction(String production_company) {
        List<Document> pipeline = new ArrayList<>();

        pipeline.add(new Document("$match",
                new Document("production_companies",
                        new Document("$regex", production_company)
                                .append("$options", "i"))));
        // Stage 1: Match documents where the "genres" field matches the provided genre

        // Now, perform the aggregation using the modified pipeline
        return movieCollection.aggregate(pipeline).into(new ArrayList<>());
    }

    //Query 3
    public default List<Object> getGenrefromTaglines() {
        List<Document> pipeline = new ArrayList<>();

        // Construct the aggregation pipeline stages
        pipeline.add(new Document("$match",
                new Document("tagline", new Document("$exists", true).append("$ne", null))
                        .append("genres", new Document("$exists", true).append("$ne", null))
                        .append("popularity", new Document("$exists", true).append("$ne", null)))); // Filter out documents with missing or null popularity

        pipeline.add(new Document("$group",
                new Document("_id", "$genres")
                        .append("taglines", new Document("$addToSet", "$tagline"))
                        .append("averagePopularity", new Document("$avg", new Document("$toDouble", "$popularity"))))); // Convert popularity to double before calculating average

        pipeline.add(new Document("$project",
                new Document("genres", "$_id")
                        .append("_id", 0L)
                        .append("taglines", 1L)
                        .append("averagePopularity", 1L)));

        // Perform aggregation and return the result
        return movieCollection.aggregate(pipeline).into(new ArrayList<>());
    }


    //Query 5
    public default List<Object> getGenresBasedOnSentiment() {
        List<Document> pipeline = Arrays.asList(
                // Project stage
                new Document("$project",
                        new Document("genre",
                                new Document("$split",
                                        Arrays.asList(
                                                new Document("$ifNull",
                                                        Arrays.asList("$genres", "")
                                                ),
                                                ", "
                                        )
                                )
                        )
                                .append("overview", new Document("$ifNull", Arrays.asList("$overview", "")))
                                .append("vote_average", new Document("$ifNull", Arrays.asList("$vote_average", "")))
                                .append("title", new Document("$ifNull", Arrays.asList("$title", "")))
                                .append("popularity", new Document("$ifNull", Arrays.asList("$popularity", "")))
                ),

                // Group stage
                new Document("$group",
                        new Document("_id", "$genre")
                                .append("count", new Document("$sum", 1L))
                                .append("avg_vote_average", new Document("$avg", new Document("$toDouble", "$vote_average")))
                                .append("avg_popularity", new Document("$avg", new Document("$toDouble", "$popularity")))
                                .append("sentiments", new Document("$push", "$$ROOT"))
                ),

                // Unwind stage
                new Document("$unwind", "$sentiments"),

                // Project stage
                new Document("$project",
                        new Document("_id", "$sentiments._id")
                                .append("genre", "$_id")
                                .append("overview", "$sentiments.overview")
                                .append("vote_average", "$sentiments.vote_average")
                                .append("title", "$sentiments.title")
                                .append("popularity", "$sentiments.popularity")
                                .append("sentiment",
                                        new Document("$cond",
                                                Arrays.asList(
                                                        new Document("$gte", Arrays.asList("$sentiments.vote_average", "$avg_vote_average")),
                                                        "positive",
                                                        "negative"
                                                )
                                        )
                                )
                ),

                // Group stage
                new Document("$group",
                        new Document("_id", "$genre")
                                .append("count", new Document("$sum", 1L))
                                .append("sentiment_count", new Document("$push", "$sentiment"))
                                .append("avg_vote_average", new Document("$avg", new Document("$toDouble", "$vote_average")))
                ),

                // Project stage
                new Document("$project",
                        new Document("genre", "$_id")
                                .append("count", 1L)
                                .append("sentiment_count",
                                        new Document("positive",
                                                new Document("$size",
                                                        new Document("$filter",
                                                                new Document("input", "$sentiment_count")
                                                                        .append("cond",
                                                                                new Document("$eq", Arrays.asList("$$this", "positive"))
                                                                        )
                                                        )
                                                )
                                        )
                                                .append("negative",
                                                        new Document("$size",
                                                                new Document("$filter",
                                                                        new Document("input", "$sentiment_count")
                                                                                .append("cond",
                                                                                        new Document("$eq", Arrays.asList("$$this", "negative"))
                                                                                )
                                                                )
                                                        )
                                                )
                                )
                                .append("avg_vote_average", "$avg_vote_average")
                )
        );
        return movieCollection.aggregate(pipeline).into(new ArrayList<>());
    }




    Optional<Movie> findMovieByImdbId(String imdbId);
    Optional<Movie> findMovieByTitle(String title);
}
