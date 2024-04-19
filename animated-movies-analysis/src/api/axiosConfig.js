import axios from  'axios';

//for cross origin
export default axios.create({
    baseURL:'http://localhost:8080/AnimatedMoviesApplication',
    headers: {"ngrok-skip-browser-warning": "true"}
});