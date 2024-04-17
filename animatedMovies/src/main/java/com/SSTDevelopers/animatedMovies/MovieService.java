package db.project.animatedMovies;
import db.project.animatedMovies.Movie;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import org.ejml.simple.SimpleMatrix;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.Properties;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    public List<Movie> allMovies(){
        return movieRepository.findAll();
    }
    public Optional<Movie> singleMovie(String imdbId)
    {
        return movieRepository.findMovieByImdbId(imdbId);
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
            for (String genre : movie.getGenres()) {
                genreTaglines.computeIfAbsent(genre, k -> new ArrayList<>()).add(movie.getTagline());
            }
        }

        // Map to hold genres and their most common words in taglines
        Map<String, List<String>> genreCommonWords = new HashMap<>();
        genreTaglines.forEach((genre, taglines) -> {
            Map<String, Integer> wordFrequency = new HashMap<>();
            taglines.stream()
                    .filter(Objects::nonNull)
                    .flatMap(tagline -> Stream.of(tokenizer.tokenize(tagline.toLowerCase())))
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
}

