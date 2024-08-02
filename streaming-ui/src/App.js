import { Route, Router, Switch } from 'react-router';
import './App.css';
import Home from './components/Home';
import VideoUpload from './components/VideoUpload';
import { createBrowserHistory } from 'history';
import VideoPlayer from './components/VideoPlayer';
import ShortsList from './components/ShortslIst';

function App() {
  const history = createBrowserHistory();
  return (
    <Router history={history}>
      <Switch>
        <Route exact path={"/videos/:id"} component={VideoPlayer} />
        <Route exact path={"/upload"} component={VideoUpload} />
        <Route exact path={"/shorts"} component={ShortsList} />
        <Route exact path={"/"} component={Home} />
      </Switch>
    </Router>
  );
}

export default App;
