package com.desarrollox.backend.api_videos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_videos.model.Video;
import java.util.Optional;

@Repository
public interface IVideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByS3Key(String s3Key);
}
