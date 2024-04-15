import axios from  'axios';

//for cross origin
export default axios.create({
    baseURL:'baseURL:http://localhost:3000/',
    headers: {"ngrok-skip-browser-warning": "true"}
});