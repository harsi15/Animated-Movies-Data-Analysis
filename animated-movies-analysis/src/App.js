import './App.css';
import api from './api/axiosConfig';
import { useState, useEffect } from 'react';


function App() {
  const [movies, setMovies] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api.get("/movies");
        setMovies(response.data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="background">

      <h3 className="subheading">Movies Grid</h3>
      <div className="movies-grid">
        {movies.map((movie, index) => (
          <div className="movie-item" key={index}>
            <img
              src={`https://image.tmdb.org/t/p/w500${movie.backdrop_path}`}
              alt={movie.title}
            />
            <div className="movie-details">
              <strong>{movie.title}</strong>
              {movie.genres.map((genre, genreIndex) => (
                <p key={genreIndex}>{genre}</p>
              ))}
              <p>Rating: {movie.vote_average} ({movie.vote_count})</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default App;
