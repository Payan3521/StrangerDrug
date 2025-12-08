package com.desarrollox.backend.api_videos.service;

import com.desarrollox.backend.api_videos.model.Video;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface IVideoService {
    Video uploadVideo(MultipartFile file);
    Video uploadPreview(MultipartFile file);
    Optional<Video> findById(Long id);
    List<Video> findAll();
    Optional<Video> delete(Long id);
    Optional<Video> updateVideo(Long id, MultipartFile file);
    Optional<Video> updatePreview(Long id, MultipartFile file);
    Optional<Video> getPlayableUrl(String videoKey, String email);
}
