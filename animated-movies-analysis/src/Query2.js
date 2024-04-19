import React, { useState, useEffect } from 'react';
import './Query.css';
import api from './api/axiosConfig';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const Query2 = () => {
  const [runtimeData, setRuntimeData] = useState([]);
  const [maxRating, setMaxRating] = useState(10); // Initialize with default maximum rating

  useEffect(() => {
    const fetchRuntimeData = async () => {
      try {
        const response = await api.get("/movies/getRuntimeAnalysisPipeline");
        setRuntimeData(response.data);

        // Calculate maximum rating
        const max = Math.max(...response.data.map(item => item.averageRating));
        setMaxRating(max);
      } catch (error) {
        console.log(error);
      }
    };
    fetchRuntimeData();
  }, []);

  return (
    <div className='query1Title'>
      <h1 className="queryHeading">Query 2</h1>
      <h3 className="queryHeading">Exploring the Influence of Ratings on Tagline Trends and Movie Runtimes Across Years</h3>
      
      <div className="chart-container">
        <ResponsiveContainer width="100%" height={400}>
          <LineChart
            data={runtimeData}
            margin={{
              top: 20,
              right: 30,
              left: 20,
              bottom: 70, // Increased bottom margin to accommodate taglines in tooltip
            }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="_id" />
            <YAxis domain={[0, maxRating]} />
            <Tooltip
              content={({ payload, label }) => {
                if (payload && payload[0] && payload[0].payload) {
                  const { taglines, averageRuntime, averageRating } = payload[0].payload;
                  return (
                    <div className="custom-tooltip">
                      <p className="tagline">{`Tagline: ${taglines.join(", ")}`}</p>
                      <p className="runtime">{`Average Runtime: ${averageRuntime}`}</p>
                      <p className="rating">{`Average Rating: ${averageRating}`}</p>
                    </div>
                  );
                }
                return null;
              }}
            />
            <Line type="monotone" dataKey="averageRuntime" stroke="#8884d8" />
            <Line type="monotone" dataKey="averageRating" stroke="#82ca9d" />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default Query2;
