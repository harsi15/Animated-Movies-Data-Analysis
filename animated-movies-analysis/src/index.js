import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Query1 from './Query1';
import Query2 from './Query2';
import Query3 from './Query3';
import Query4 from './Query4';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';


const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  
  <React.StrictMode>
    <Navbar className="navbartop">
        <Container>
          <Navbar.Brand href="/">Animated Movies Analysis</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link href="/query1">Query1</Nav.Link>
              <Nav.Link href="/query2">Query2</Nav.Link>
              <Nav.Link href="/query3">Query3</Nav.Link>
              <Nav.Link href="/">Query4</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
        </Navbar>
    <BrowserRouter>
      <Routes>
      
        <Route path="/*" element={<App />}/>
        <Route path="/query1" element={<Query1/>}/>
        <Route path="/query2" element={<Query2/>}/>
        <Route path="/query3" element={<Query3/>}/>
      </Routes>  
    </BrowserRouter>
    
  </React.StrictMode>
);

