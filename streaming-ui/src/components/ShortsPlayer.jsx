// src/components/VideoPlayer.js
import React, { useRef } from "react";
import ReactPlayer from "react-player";

const ShortsPlayer = ({ src, playing }) => {
    const playerRef= useRef();
  return (
    <div className="d-flex justify-content-center align-items-center video-container">
      <ReactPlayer
        ref={playerRef}
        className="video-player-wrapper rounded shadow"
        url={src}
        playing={playing}
        controls
        width="360px"
        height="800px"
      />
    </div>
  );
};

export default ShortsPlayer;
