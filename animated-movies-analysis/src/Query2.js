import React, { useState, useEffect } from 'react';
import './Query.css';
import api from './api/axiosConfig';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const Query2 = () => {
  const [movies, setMovies] = useState([]);

  useEffect(() => {
    const getMovies = async () => {
      try {
        const response = await api.get("/movies/taglineImpactOnRevenue");
        console.log(response.data);
        setMovies(response.data);
      } catch (err) {
        console.log(err);
      }
    }
    getMovies();
  }, []);

  return (
    <div className='query1Title'>
      <h1 className="queryHeading">Query 2</h1>
      <p className="queryHeading">Assess Tagline Impact on Revenue Across Genres</p>
      
      <div className="chart-container">
        <ResponsiveContainer width="100%" height={400}>
          <BarChart
            width={500}
            height={300}
            data={movies}
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
            <Tooltip />
            <Bar dataKey="average_revenue" fill="#8884d8" />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default Query2;
