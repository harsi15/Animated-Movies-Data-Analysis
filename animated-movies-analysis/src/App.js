import './App.css';
import api from './api/axiosConfig';
import { useState, useEffect } from 'react';
import Dropdown from 'react-bootstrap/Dropdown';
import { MDBInputGroup, MDBInput, MDBIcon, MDBBtn } from 'mdb-react-ui-kit';

function App() {
  const [movies, setMovies] = useState([]);

  useEffect(() => {
    fetchData(); // Fetch data initially
  }, []);

  const fetchData = async (filter = "") => {
    try {
      // Append filter if provided
      const response = await api.get(`/movies/getFilterBasedOnGenre${filter ? `/${filter}` : ""}`);
      setMovies(response.data);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  const handleFilterSelect = async (filter) => {
    try {
      const response = await api.get(`/movies/getFilterBasedOnGenre/${filter}`);
      console.log("Response:", response.data); // Log the response data
      // Split genres string into an array
      const moviesWithGenresArray = response.data.map(movie => {
        return {
          ...movie,
          genres: movie.genres.split(',').map(genre => genre.trim())
        };
      });
      setMovies(moviesWithGenresArray);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  const handleProductionFilterSelect = async (filter) => {
    try {
      const response = await api.get(`/movies/getFilterBasedOnProduction/${filter}`);
      console.log("Response:", response.data); // Log the response data
      // Split genres string into an array
      const moviesWithGenresArray = response.data.map(movie => {
        return {
          ...movie,
          genres: movie.production_companies.split(',').map(genre => genre.trim())
        };
      });
      setMovies(moviesWithGenresArray);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };


  return (
    <div className="background">
      <h1 className="subheading">Movies</h1>
      <div className="dropdown-container">
      <Dropdown className="dropdownButton">
        <Dropdown.Toggle className="toggleButton" variant="success" id="dropdown-basic">
          <b>Genres</b>
        </Dropdown.Toggle>

        <Dropdown.Menu>
          <Dropdown.Item onClick={() => handleFilterSelect('Family')}>Family</Dropdown.Item>
          <Dropdown.Item onClick={() => handleFilterSelect('Action')}>Action</Dropdown.Item>
          <Dropdown.Item onClick={() => handleFilterSelect('Animation')}>Animation</Dropdown.Item>
          <Dropdown.Item onClick={() => handleFilterSelect('Horror')}>Horror</Dropdown.Item>
          <Dropdown.Item onClick={() => handleFilterSelect('Comedy')}>Comedy</Dropdown.Item>
          <Dropdown.Item onClick={() => handleFilterSelect('Drama')}>Drama</Dropdown.Item>
          <Dropdown.Item onClick={() => handleFilterSelect('Adventure')}>Adventure</Dropdown.Item>
          <Dropdown.Item onClick={() => handleFilterSelect('Fantasy')}>Fantasy</Dropdown.Item>
          <Dropdown.Item onClick={() => handleFilterSelect('Science Fiction')}>Science Fiction</Dropdown.Item>
          <Dropdown.Item onClick={() => handleFilterSelect('Music')}>Music</Dropdown.Item>
          <Dropdown.Item onClick={() => handleFilterSelect('Romance')}>Romance</Dropdown.Item>

        </Dropdown.Menu>
      </Dropdown>
      <Dropdown>
        <Dropdown.Toggle className="toggleButton" variant="success" id="dropdown-basic">
          <b>Production Companies</b>
        </Dropdown.Toggle>

        <Dropdown.Menu>
          <Dropdown.Item onClick={() => handleProductionFilterSelect('Pixar')}>Pixar</Dropdown.Item>
          <Dropdown.Item onClick={() => handleProductionFilterSelect('Walt Disney Pictures')}>Walt Disney Pictures</Dropdown.Item>
          <Dropdown.Item onClick={() => handleProductionFilterSelect('Walt Disney Feature Animation')}>Walt Disney Feature Animation</Dropdown.Item>
          <Dropdown.Item onClick={() => handleProductionFilterSelect('DreamWorks Pictures')}>DreamWorks Pictures</Dropdown.Item>
          <Dropdown.Item onClick={() => handleProductionFilterSelect('Pacific Data Images')}>Pacific Data Images</Dropdown.Item>
          <Dropdown.Item onClick={() => handleProductionFilterSelect('Illumination')}>Illumination</Dropdown.Item>
          <Dropdown.Item onClick={() => handleProductionFilterSelect('Columbia Pictures')}>Columbia Pictures</Dropdown.Item>
          <Dropdown.Item onClick={() => handleProductionFilterSelect('Pascal Pictures')}>Pascal Pictures</Dropdown.Item>
          <Dropdown.Item onClick={() => handleProductionFilterSelect('Arad Productions')}>Arad Productions</Dropdown.Item>
          <Dropdown.Item onClick={() => handleProductionFilterSelect('Lord Miller')}>Lord Miller</Dropdown.Item>
          <Dropdown.Item onClick={() => handleProductionFilterSelect('20th Century Fox')}>20th Century Fox</Dropdown.Item>
          <Dropdown.Item onClick={() => handleProductionFilterSelect('Blue Sky Studios')}>Blue Sky Studios</Dropdown.Item>
          
        </Dropdown.Menu>

      </Dropdown>

      
      </div>
      <div className="movies-grid">
      {movies.map((movie, index) => (
      <div className="movie-item" key={index}>
        <img
          src={`https://image.tmdb.org/t/p/w500${movie.backdrop_path}`}
          alt={movie.title}
        />
        <div className="movie-details">
          <strong><b>{movie.title}</b></strong>
          {Array.isArray(movie.genres) ? (
            movie.genres.map((genre, genreIndex) => (
              <p key={genreIndex}>{genre}</p>
            ))
          ) : (
            <p>No genres available</p>
          )}
          <p>Rating: {movie.vote_average} ({movie.vote_count})</p>
        </div>
      </div>
    ))}
      </div>
    </div>
  );
}

export default App;
