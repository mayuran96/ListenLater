'use strict';

import React, { Component } from 'react';
import { render } from 'react-dom';
import { BrowserRouter, Route, Redirect } from 'react-router-dom';

import Landing from './components/Landing';


class App extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
        <BrowserRouter>
          <div>
            <Route exact path="/" component={Landing} />
          </div>
        </BrowserRouter>
    );
  }
}

export default App;