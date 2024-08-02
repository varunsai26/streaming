import axios from "axios";
import React, { useEffect, useLayoutEffect, useState } from "react";
import ReactHlsPlayer from "react-hls-player";
import { withRouter } from "react-router";
import { API_URL } from "../Constants/StreamingConstants";
import { Dropdown, DropdownButton } from "react-bootstrap";

const VideoPlayer = ({ match, location }) => {
  const qualityMapping = {
    MEDIUM: "480",
    HD: "720",
    FULL_HD: "1080",
  };

  const [video, setVideo] = useState({});
  const { id } = match.params;
  const hlsBaseUrl = `${API_URL}videoplayback/${id}/`;
  const [availableQualities, setAvailableQualities] = useState(["480"]);
  const [selectedQuality, setSelectedQuality] = useState("480");

  const handleChangeQuality = (quality) => {
    setSelectedQuality(quality);
  };

  const getVideo = (id) => {
    axios
      .get(`${API_URL}api/videos/${id}`)
      .then((response) => {
        console.log(response);
        setVideo(response.data);
      })
      .catch((error) => {
        console.error(error);
        alert("No video found");
        setVideo({});
      });
  };

  useLayoutEffect(() => {
    if (location?.state?.video) {
      setVideo(location.state.video);
    } else {
      getVideo(id);
    }
  }, []);

  useEffect(() => {
    if (video && video.qualities) {
      setAvailableQualities(video.qualities.map((q) => qualityMapping[q]));
    }
  }, [video]);

  const hlsUrl = `${hlsBaseUrl}${selectedQuality}/sample.m3u8`;

  return (
    <div className="video-player-container">
      <h2>Video Player</h2>
      <div className="d-flex justify-content-center">
        <ReactHlsPlayer
          hlsConfig={{
            maxLoadingDelay: 4,
            minAutoBitrate: 0,
            lowLatencyMode: true,
          }}
          className="video-player"
          src={hlsUrl}
          autoPlay={false}
          controls={true}
          width="100%"
          height="auto"
        />
      </div>
      <div className="quality-dropdown d-flex justify-content-center mt-3">
        <DropdownButton
          id="quality-dropdown"
          title={`${selectedQuality}p`}
          variant="primary"
        >
          {availableQualities.map((quality) => (
            <Dropdown.Item
              key={quality}
              onClick={() => handleChangeQuality(quality)}
              active={selectedQuality === quality}
            >
              {quality}p
            </Dropdown.Item>
          ))}
        </DropdownButton>
      </div>
    </div>
  );
};

export default withRouter(VideoPlayer);
