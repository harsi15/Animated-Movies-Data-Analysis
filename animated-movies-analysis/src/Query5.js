import React, { useState, useEffect } from 'react';
import './Query.css';
import api from './api/axiosConfig';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts';

const Query5 = () => {
  const [genresData, setGenresData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api.get("/movies/getGenresBasedOnSentiment");
        console.log(response.data);
        setGenresData(response.data);
      } catch (error) {
        console.log(error);
      }
    };

    fetchData();
  }, []);

  // Prepare data for bar chart
  const chartData = genresData.map((genreData) => ({
    genre: Array.isArray(genreData._id) ? genreData._id.join(', ') : genreData._id, // Check if _id is an array
    positiveSentiment: genreData.sentiment_count.filter(sentiment => sentiment.positive).length,
    negativeSentiment: genreData.sentiment_count.filter(sentiment => sentiment.negative).length,
    avgVoteAverage: genreData.avg_vote_average // Corrected property name
  }));

  return (
    <div className="query1Title">
      <h1 className="queryHeading">Query 5</h1>
      <h3 className="queryHeading">Explore how movie genres correlate with positive sentiment counts and average vote average in their overviews</h3>
      <div className="bar-chart-container">
        <ResponsiveContainer width="100%" height={400}>
          <BarChart data={chartData} margin={{ top: 20, right: 30, left: 20, bottom: 50 }}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="genre" />
            <YAxis label={{ value: 'Sentiment Count', angle: -90, position: 'insideLeft' }} />
            <Tooltip content={<CustomTooltip />} />
            <Legend />
            <Bar dataKey="positiveSentiment" name="Positive Sentiment" fill="lightblue" />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

// Custom tooltip component to display vote average
const CustomTooltip = ({ active, payload, label }) => {
  if (active && payload && payload.length) {
    return (
      <div className="custom-tooltip">
        <p className="label">{`Genre: ${label}`}</p>
        <p className="sentiment">{`Positive Sentiment: ${payload[0].value}`}</p>
        <p className="vote-average">{`Average Vote Average: ${payload[0].payload.avgVoteAverage}`}</p> {/* Corrected access to avgVoteAverage */}
      </div>
    );
  }
  return null;
};

export default Query5;
