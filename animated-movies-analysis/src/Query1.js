import React, { useState, useEffect } from 'react';
import './Query.css';
import api from './api/axiosConfig';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts';

const Query1 = () => {
  const [movies, setMovies] = useState([]);

  useEffect(() => {
    const getMovies = async () => {
      try {
        const response = await api.get("/movies/taglineImpactOnRevenue");
        setMovies(response.data);
      } catch (err) {
        console.log(err);
      }
    }
    getMovies();
  }, []);

  return (
    <div className='query1Title'>
      <h1 className="queryHeading">Query 1</h1>
      <h3 className="queryHeading">Investigating Revenue, Budget, and Their Discrepancies Across Genres Alongside Tagline Count</h3>
      <br></br>
      <div className="chart-container">
        <ResponsiveContainer width="100%" height={400}>
          <BarChart
            width={500}
            height={300}
            data={movies}
            margin={{
              top: 5,
              right: 70,
              left: 75,
              bottom: 70, // Increased bottom margin to accommodate tooltip
            }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="genres" label={{ value: 'Genres', position: 'insideBottom', offset: -50 }} />
            <YAxis label={{ value: 'Amount', angle: -90, position: 'insideLeft', offset: -55 }} />
            <Tooltip
              content={({ payload, label }) => {
                if (payload && payload[0] && payload[0].payload) {
                  const { average_revenue, average_budget, tagline_count } = payload[0].payload;
                  const revenue = parseFloat(average_revenue);
                  const budget = parseFloat(average_budget);
                  const difference = (revenue - budget).toFixed(2);
                  return (
                    <div className="custom-tooltip">
                      <p className="genre">{`Genre: ${label}`}</p>
                      <p className="revenue">{`Average Revenue: $${average_revenue}`}</p>
                      <p className="budget">{`Average Budget: $${average_budget}`}</p>
                      <p className="difference">{`Difference (Revenue - Budget): $${difference}`}</p>
                      <p className="tagline">{`Tagline Count: ${tagline_count}`}</p>
                    </div>
                  );
                }
                return null;
              }}
            />
            <Bar dataKey="average_budget" stackId="stack" fill="#82ca9d" />
            <Bar dataKey="average_revenue" stackId="stack" fill="#8884d8" />
            <Legend />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default Query1;
