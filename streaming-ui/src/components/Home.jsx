import React from "react";
import { Button, Form } from "react-bootstrap";
import VideoList from "./VideoList";

const Home = ({history}) => {
  return (
    <div className="m-3">
      <div className="d-flex p-2">
        <Form.Control type="text" className="p-3" placeholder="search" />
        <Button className="m-2 p-2" onClick={()=>history.push("/upload")}>upload</Button>
      </div>
      <VideoList />
    </div>
  );
};

export default Home;
