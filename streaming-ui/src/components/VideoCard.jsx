import React from "react";
import { Card, Image } from "react-bootstrap";
import { withRouter } from "react-router";
import { API_URL } from "../Constants/StreamingConstants";

const VideoCard = ({ video, history }) => {

  const handleClick = () => {
    // Navigate to the video route with video ID
    history.push({
      pathname: `/videos/${video.id}`,
      state: { video },
    });
  };

  const formatDuration = (seconds) => {
    if (isNaN(seconds) || seconds < 0) {
      return "00";
    }

    // Round down total seconds
    seconds = Math.floor(seconds);

    // Calculate hours, minutes, and seconds
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const remainingSeconds = Math.floor(seconds % 60);

    // Format hours, minutes, and seconds to always be two digits
    const formattedHours = hours.toString().padStart(2, "0");
    const formattedMinutes = minutes.toString().padStart(2, "0");
    const formattedSeconds = remainingSeconds.toString().padStart(2, "0");

    // Return formatted duration
    if (hours > 0) {
      return `${formattedHours}:${formattedMinutes}:${formattedSeconds}`;
    } else if (minutes > 0) {
      return `${formattedMinutes}:${formattedSeconds}`;
    } else {
      return formattedSeconds;
    }
  };

  return (
    <Card onClick={handleClick} className="mb-3" style={{ cursor: "pointer" }}>
      <Card.Body className="d-flex flex-column flex-md-row">
        <Image
          src={`${API_URL}thumbnail/${video.thumbnail}`}
          thumbnail
          className="me-3"
        />
        <div>
          <Card.Title>{video.name}</Card.Title>
          {video.description && <Card.Text>{video.description}</Card.Text>}
          <Card.Text>
            <small className="text-muted">
              {formatDuration(video.duration)}
            </small>
          </Card.Text>
        </div>
      </Card.Body>
    </Card>
  );
};

export default withRouter(VideoCard);
