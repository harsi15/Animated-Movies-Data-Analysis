package com.SSTDevelopers.animatedMovies;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bson.Document;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/AnimatedMoviesApplication/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;
    private MovieRepository movieRepository;


    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies()
    {
        return new ResponseEntity<List<Movie>>(movieService.allMovies(), HttpStatus.OK);
    }
    @GetMapping("/{imdbId}")
    public ResponseEntity<Optional<Movie>> getSingleMovie(@PathVariable String imdbId) {
        return new ResponseEntity<Optional<Movie>>(movieService.singleMovie(imdbId),HttpStatus.OK);
    }

    @GetMapping("/{title}")
    public ResponseEntity<Optional<Movie>> getSingleMovieByTitle(@PathVariable String title) {
        return new ResponseEntity<Optional<Movie>>(movieService.singleMovieByTitle(title),HttpStatus.OK);
    }
    @GetMapping("/tagline-themes-by-genre")
    public ResponseEntity<Map<String, List<String>>> getTaglineThemesByGenre() {
        Map<String, List<String>> themes = movieService.analyzeTaglinesByGenre();
        return new ResponseEntity<>(themes, HttpStatus.OK);
    }

    @GetMapping("/taglineImpactOnRevenue")
    public ResponseEntity<List<Object>> getTaglineImpactOnRevenue() {
        List<Object> themes = movieService.taglineImpactOnRevenue();
        return new ResponseEntity<>(themes, HttpStatus.OK);
    }

    @GetMapping("/getRuntimeAnalysisPipeline")
    public ResponseEntity<List<Object>> getRuntimeAnalysisPipeline() {
        List<Object> themes = movieService.runtimeAnalysisPipeline();
        return new ResponseEntity<>(themes, HttpStatus.OK);
    }

    @GetMapping("/getStorytellingPatterns")
    public ResponseEntity<List<Object>> getStorytellingPatterns() {
        List<Object> themes = movieService.storytellingPatterns();
        return new ResponseEntity<>(themes, HttpStatus.OK);
    }

}
