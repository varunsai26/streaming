import axios from 'axios';
import React, { useState } from 'react';
import { Button, Form, Container, Row, Col, Card, Spinner } from 'react-bootstrap';
import { API_URL } from '../Constants/StreamingConstants';

const VideoUpload = () => {
  const [file, setFile] = useState(null);
  const [duration, setDuration] = useState(null);
  const [resolution, setResolution] = useState(null);
  const [loading, setLoading] = useState(false); // State for loading

  const handleFileChange = (event) => {
    const selectedFile = event.target.files[0];
    if (selectedFile) {
      setFile(selectedFile);

      const videoElement = document.createElement('video');
      videoElement.preload = 'metadata';
      videoElement.onloadedmetadata = () => {
        setDuration(videoElement.duration);
        setResolution(Math.max(videoElement.videoWidth, videoElement.videoHeight));
        URL.revokeObjectURL(videoElement.src);
      };
      videoElement.src = URL.createObjectURL(selectedFile);
    } else {
      alert("No file selected");
    }
  };

  function getThumbnailFromVideo(file, time = 0) {
    return new Promise((resolve, reject) => {
      const video = document.createElement('video');
      video.src = URL.createObjectURL(file);

      video.addEventListener('loadeddata', () => {
        video.currentTime = time; // Set the time to capture the thumbnail
      });

      video.addEventListener('seeked', () => {
        const canvas = document.createElement('canvas');
        canvas.width = 150; // Fixed width for the thumbnail
        canvas.height = 96; // Fixed height for the thumbnail
        const context = canvas.getContext('2d');
        context.drawImage(video, 0, 0, canvas.width, canvas.height);

        // Convert canvas to image URL
        const thumbnailUrl = canvas.toDataURL('image/jpeg');
        resolve(thumbnailUrl);

        // Clean up
        URL.revokeObjectURL(video.src);
      });

      video.addEventListener('error', () => {
        reject(new Error('Failed to extract thumbnail'));
      });
    });
  }

  function dataURLToBlob(dataURL) {
    const byteString = atob(dataURL.split(',')[1]);
    const mimeString = dataURL.split(',')[0].split(':')[1].split(';')[0];
    const buffer = new ArrayBuffer(byteString.length);
    const data = new Uint8Array(buffer);
    for (let i = 0; i < byteString.length; i++) {
        data[i] = byteString.charCodeAt(i);
    }
    return new Blob([buffer], { type: mimeString });
}


  const handleUpload = async () => {
    if (!file) {
      alert("No file selected");
      return;
    }

    setLoading(true); // Set loading to true when upload starts

    const formData = new FormData();
    const thumbnailUrl = await getThumbnailFromVideo(file, 1);
    const thumbnailBlob = dataURLToBlob(thumbnailUrl);
    formData.append('thumbnail',thumbnailBlob,'thumbnail.jpg');
    formData.append('file', file);
    formData.append('duration', duration);
    formData.append('resolution', resolution);

    try {
      await axios.post(`${API_URL}api/videos/upload`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      alert('File uploaded successfully!');
    } catch (error) {
      alert('Failed to upload file: ' + error.message);
    } finally {
      setLoading(false); // Reset loading state after upload completes
    }
  };

  return (
    <Container className="my-5">
      <Row className="justify-content-center">
        <Col md={6}>
          <Card>
            <Card.Body>
              <Card.Title className="text-center">Upload Video</Card.Title>
              <Form>
                <Form.Group className="mb-3" controlId="formFile">
                  <Form.Label>Select video file</Form.Label>
                  <Form.Control type="file" onChange={handleFileChange} />
                </Form.Group>
                <Button
                  variant="primary"
                  onClick={handleUpload}
                  block
                  disabled={loading} // Disable button while loading
                >
                  {loading ? (
                    <span>
                      <Spinner animation="border" size="sm" /> Uploading...
                    </span>
                  ) : (
                    'Upload'
                  )}
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default VideoUpload;
