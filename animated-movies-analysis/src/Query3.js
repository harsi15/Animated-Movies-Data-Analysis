import React, { useState, useEffect } from 'react';
import './Query.css';
import api from './api/axiosConfig';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const Query4 = () => {
  const [runtimeData, setRuntimeData] = useState([]);

  useEffect(() => {
    const fetchRuntimeData = async () => {
      try {
        const response = await api.get("/movies/getRuntimeAnalysisPipeline");
        setRuntimeData(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    fetchRuntimeData();
  }, []);

  return (
    <div className='query1Title'>
      <h1 className="queryHeading">Query 3</h1>
      <p className="queryHeading">Discover Trends in Movie Runtime Over the Years</p>
      
      <div className="chart-container">
        <ResponsiveContainer width="100%" height={400}>
          <LineChart
            data={runtimeData}
            margin={{
              top: 5,
              right: 30,
              left: 20,
              bottom: 5,
            }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="_id" />
            <YAxis yAxisId="left" />
            <YAxis yAxisId="right" orientation="right" />
            <Tooltip />
            <Line type="monotone" dataKey="averageRuntime" stroke="#8884d8" yAxisId="left" />
            <Line type="monotone" dataKey="averageRating" stroke="#82ca9d" yAxisId="right" />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default Query4;
