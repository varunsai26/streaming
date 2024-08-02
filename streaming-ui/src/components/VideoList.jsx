// src/components/VideoList.js
import React, { useLayoutEffect, useState } from "react";
import { Container, ListGroup } from "react-bootstrap";
import VideoCard from "./VideoCard";
import axios from "axios";
import { API_URL } from "../Constants/StreamingConstants";
const VideoList = () => {
  const [videos, setVideos] = useState([]);
  const getVideos = () => {
    axios
      .get(`${API_URL}/api/videos/`)
      .then((response) => {
        console.log(response);
        setVideos(response.data);
      })
      .catch((error) => {
        console.error(error);
        alert("No videos found");
        setVideos([]);
      });
  };
  useLayoutEffect(() => {
    getVideos();
  }, []);
  return (
    <Container>
      <ListGroup>
        {videos.map((video, index) => (
          <ListGroup.Item key={index}>
            <VideoCard video={video} />
          </ListGroup.Item>
        ))}
      </ListGroup>
    </Container>
  );
};

export default VideoList;
