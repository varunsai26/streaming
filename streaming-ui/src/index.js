import 'bootstrap/dist/css/bootstrap.min.css';
import { createBrowserHistory } from 'history';
import React from 'react';
import { render } from 'react-dom'; // <- This is the correct import statement for React version 17
import App from './App';
import './index.css';
import { Router } from 'react-router-dom';
const root = document.getElementById('root'); // <- This is the correct method call for React version 17
const history = createBrowserHistory();

render(
  <React.StrictMode>
     <Router history={history}>
    <App />
    </Router>
  </React.StrictMode>,
  root
);
