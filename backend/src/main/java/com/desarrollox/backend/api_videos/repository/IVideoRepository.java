package com.desarrollox.backend.api_videos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_videos.model.Video;

@Repository
public interface IVideoRepository extends JpaRepository<Video, Long> {
    
}
