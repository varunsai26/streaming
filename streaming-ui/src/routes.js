import { HashRouter, Route, Switch } from "react-router-dom";
import Home from "./components/Home";
import ShortsList from "./components/ShortslIst";
import VideoPlayer from "./components/VideoPlayer";
import VideoUpload from "./components/VideoUpload";

export default () => (
    <HashRouter>
        <Switch>
            <Route exact path={"/videos/:id"} component={VideoPlayer} />
            <Route exact path={"/upload"} component={VideoUpload} />
            <Route exact path={"/shorts"} component={ShortsList} />
            <Route exact path={"/"} component={Home} />
        </Switch>
    </HashRouter>
)

