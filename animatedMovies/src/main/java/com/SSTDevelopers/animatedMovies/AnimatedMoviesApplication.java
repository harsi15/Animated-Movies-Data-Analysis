package db.project.animatedMovies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AnimatedMoviesApplication {
	public static void main(String[] args) {
		SpringApplication.run(AnimatedMoviesApplication.class, args);
	}

}
