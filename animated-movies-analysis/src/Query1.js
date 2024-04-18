import React, { useState, useEffect } from 'react';
import WordCloud from 'react-wordcloud';
import './Query.css';
import api from './api/axiosConfig';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip } from 'recharts';


const Query2 = () => {
  const [movies, setMovies] = useState();

  useEffect(() => {
    const getMovies = async () => {
      try {
        const response = await api.get("/movies/tagline-themes-by-genre");
        console.log(response.data);
        setMovies(response.data);
      } catch (err) {
        console.log(err);
      }
    }
    getMovies();
  }, []);

  const wordCounts = {};

  if (movies) {
    Object.values(movies).forEach(genreWords => {
      genreWords.forEach(word => {
        wordCounts[word] = (wordCounts[word] || 0) + 1;
      });
    });
  }

  const words2 = Object.entries(wordCounts).map(([word, count]) => ({
    text: word,
    value: count
  }));

  const options = {
    fontSizes: [20, 60], // Set font size range
    fontWeight: 'bold', // Set font weight to bold
    // You can customize other options here, such as width, height, colors, etc.
  };

  return (
    
    <div className='query1Title'>
      
      <h1 className="queryHeading">Query 1</h1>
      <p className="queryHeading">Discover Common Themes in Movie Taglines Across Genres</p>

      <div  style={{ display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
        {/* Display WordCloud */}
        <div className="wordcloud" style={{ width: '50%', height: '600px' }}>
          <WordCloud words={words2} options={options} />
        </div>

        {/* Display BarChart */}
        <div className="barchart" style={{ width: '50%', height: '300px' }}>
          <BarChart
            width={700}
            height={300}
            data={words2}
            margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="text" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="value" fill="#8884d8" />
          </BarChart>
        </div>
      </div>
    </div>
  );
};

export default Query2;
