package com.SSTDevelopers.animatedMovies;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

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
//    @Field("id")
//    private String recordId;
    private String title;
    private List<String> genres;
    private String overview;
    private String tagline;
    private Double revenue;
    private Double vote_average;

    private Integer vote_count;
    private String backdrop_path;
    private Integer runtime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date release_date;
    private Double popularity;
    private Double sentiment;

    public Movie(String title, int runtime, Date release_date, double vote_average, String overview, String backdrop_path, Integer vote_count, List<String> genres) {
        this.title = title;
        this.runtime = runtime;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.overview = overview;
        this.backdrop_path = backdrop_path;
        this.vote_count = vote_count;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public int getRuntime() {
        return runtime;
    }

    public Date getReleaseDate() {
        return release_date;
    }

    public double getVoteAverage() {
        return vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public Double getSentiment() {
        return sentiment;
    }


    public void setSentiment(Double sentiment) {
        this.sentiment = sentiment;
    }

    public String getimdbId() {
        return imdbId;
    }

    public void setimdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public List<String> getGenre() {
        return genres;
    }

    public void setGenre(List<String> genres) {
        this.genres = genres;
    }

    public Double getRevenue() {
        return revenue;
    }
    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }
}
