import React, { useState, useEffect } from "react";
import ShortsPlayer from "./ShortsPlayer";

const videos = [
  { id: 1, src: "https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd" },
  { id: 2, src: "https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd" },
  { id: 3, src: "https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd" },
];

const ShortsList = () => {
    const [currentVideo, setCurrentVideo] = useState(0);

    const handleScroll = () => {
      const scrollPosition = window.pageYOffset + window.innerHeight / 2;
      const videoIndex = Math.floor(scrollPosition / window.innerHeight);
      if (videoIndex !== currentVideo && videoIndex < videos.length) {
        setCurrentVideo(videoIndex);
      }
    };
  
    useEffect(() => {
      window.addEventListener('scroll', handleScroll);
      return () => {
        window.removeEventListener('scroll', handleScroll);
      };
    }, [currentVideo]);
  
    return (
        <ShortsPlayer src={videos[currentVideo].src} playing={true} />
    );
};

export default ShortsList;
