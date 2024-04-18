package com.SSTDevelopers.animatedMovies;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> allMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> singleMovie(String imdbId) {
        return movieRepository.findMovieByImdbId(imdbId);
    }

    public Optional<Movie> singleMovieByTitle(String title) {
        return movieRepository.findMovieByTitle(title);
    }

    public Map<String, List<String>> analyzeTaglinesByGenre() {
        List<Movie> movies = movieRepository.findAll();
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        Set<String> stopwords = new HashSet<>(Arrays.asList(",", ".", "!", "'", "the", "a", "of", "to", "-", "s", "and", "in", "for", "on",
                "with", "as", "is", "that", "it", "at", "by", "from", "be","they","an", "...", "you", "your", "all", "be", "can", "was",
                "what", "ll", "get", "t", "?", "he", "will", "not", "this", "but", "no", "there", "are", "their", "when",
                "i", "we", "than", "those", "about", "its", "into", "€"));

        // Create a map to hold genres and associated taglines
        Map<String, List<String>> genreTaglines = new HashMap<>();
        for (Movie movie : movies) {
            for (String genre : movie.getGenre()) {
                genreTaglines.computeIfAbsent(genre, k -> new ArrayList<>()).add(movie.getTagline());
            }
        }

        // Map to hold genres and their most common words in taglines
        Map<String, List<String>> genreCommonWords = new HashMap<>();
        genreTaglines.forEach((genre, taglines) -> {
            Map<String, Integer> wordFrequency = new HashMap<>();
            taglines.stream()
                    .filter(Objects::nonNull)
                    .flatMap(tagline -> Arrays.stream(tokenizer.tokenize(tagline.toLowerCase())))
                    .filter(token -> !stopwords.contains(token) && token.trim().length() > 0)
                    .forEach(token -> wordFrequency.put(token, wordFrequency.getOrDefault(token, 0) + 1));

            List<String> commonWords = wordFrequency.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .limit(10) // Top 10 words
                    .collect(Collectors.toList());

            genreCommonWords.put(genre, commonWords);
        });

        return genreCommonWords;
    }

    public List<Object> taglineImpactOnRevenue() {
        return movieRepository.getRevenueBasedOnGenre();
    }

    public List<Object> runtimeAnalysisPipeline() {
        return movieRepository.getRuntimeAnalysisPipeline();
    }

    public List<Object> storytellingPatterns() {
        return movieRepository.getStorytellingPatterns();
    }
}
