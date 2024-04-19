import React, { useState, useEffect } from 'react';
import './Query.css';
import api from './api/axiosConfig';
import WordCloud from 'react-wordcloud';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const Query3 = () => {
  const [taglineData, setTaglineData] = useState([]);
  const [barChartData, setBarChartData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api.get("/movies/getGenrefromTaglines");
        console.log(response.data);

        // Extract tagline data for word cloud
        const taglines = response.data.map(movie => movie.taglines).flat();
        const taglineCounts = {};
        taglines.forEach(tagline => {
          if (taglineCounts[tagline]) {
            taglineCounts[tagline]++;
          } else {
            taglineCounts[tagline] = 1;
          }
        });
        const wordCloudData = Object.keys(taglineCounts).map(tagline => ({
          text: tagline,
          value: taglineCounts[tagline],
        }));
        setTaglineData(wordCloudData);

        // Extract data for bar chart and convert popularity to percentage
        const barChartData = response.data.map(movie => {
          // Convert popularity to percentage
          let popularityPercent = (movie.averagePopularity / 10); // Convert popularity to percentage
          if (popularityPercent > 100) popularityPercent = 100; // Limit popularity to 100

          return {
            genres: movie.genres,
            taglineCount: movie.taglines.length,
            popularity: popularityPercent,
          };
        });
        setBarChartData(barChartData);
      } catch (err) {
        console.log(err);
      }
    };
    fetchData();
  }, []);

  return (
    <div className='query1Title'>
      <h1 className="queryHeading">Query 3</h1>
      <h3 className="queryHeading">Unveiling Popularity and Shared Traits in Movie Taglines Across Various Genres</h3>

      <div className="chart-container">
        {/* Word Cloud */}
        <div className="word-cloud-container">
          <WordCloud
            words={taglineData}
            options={{
              fontSizes: [20, 60], // Set font size range
              rotations: 0,
              rotationAngles: [0, 0],
              scale: 'sqrt',
              spiral: 'archimedean',
            }}
          />
        </div>

        {/* Bar Chart */}
        <div className="bar-chart-container">
          <ResponsiveContainer width="100%" height={400}>
            <BarChart
              data={barChartData}
              margin={{
                top: 5,
                right: 30,
                left: 20,
                bottom: 5,
              }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="genres" />
              <YAxis />
              <Tooltip content={<CustomTooltip />} />
              <Bar dataKey="popularity" fill="#82ca9d" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};

// Custom Tooltip Component
const CustomTooltip = ({ active, payload }) => {
  if (active && payload && payload.length) {
    const data = payload[0].payload;
    return (
      <div className="custom-tooltip">
        <p>{`${data.genres}`}</p>
        <p>{`Popularity: ${data.popularity} %`}</p>
        <p>{`Tagline Count: ${data.taglineCount}`}</p>
      </div>
    );
  }
  return null;
};

export default Query3;
