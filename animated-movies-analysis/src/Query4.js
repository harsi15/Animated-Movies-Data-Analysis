import React, { useState, useEffect } from 'react';
import './Query.css';
import api from './api/axiosConfig';
import WordCloud from 'react-wordcloud';

const Query4 = () => {
  const [genresData, setGenresData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api.get("/movies/getStorytellingPatterns");
        console.log(response.data);
        setGenresData(response.data);
      } catch (error) {
        console.log(error);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="query1Title">
    <h1 className="queryHeading">Query 4</h1>
      <h3 className="queryHeading">Explore Genre-Specific Storytelling Patterns in Overviews and Taglines</h3>
      <div className="word-cloud-container">
        {genresData.map((genreData) => (
          <div key={genreData._id}>
            <h4 className="queryHeading">{genreData.genre}</h4>
            <WordCloud
              words={getWordCloudData(genreData)}
              options={{
                rotations: 0,
                rotationAngles: [0, 0],
                scale: 'sqrt',
                spiral: 'archimedean',
              }}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

const getWordCloudData = (genreData) => {
  const wordCloudData = [];

  // Add narrative structures
  genreData.narrativeStructures.forEach((text) => {
    const words = text.split(" ");
    words.forEach((word) => {
      wordCloudData.push({ text: word, value: 5 });
    });
  });

  // Add plot elements
  genreData.plotElements.forEach((text) => {
    const words = text.split(" ");
    words.forEach((word) => {
      wordCloudData.push({ text: word, value: 3 });
    });
  });

  // Add thematic motifs
  genreData.thematicMotifs.forEach((text) => {
    const words = text.split(" ");
    words.forEach((word) => {
      wordCloudData.push({ text: word, value: 2 });
    });
  });

  return wordCloudData;
};

export default Query4;
