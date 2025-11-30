package com.desarrollox.backend.api_photos.service;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend.api_photos.model.Photo;
import org.springframework.web.multipart.MultipartFile;

public interface IPhotoService {
    Photo uploadThumbnail(MultipartFile file);
    Photo uploadProfile(MultipartFile file);
    Optional<Photo> findById(Long id);
    List<Photo> findAll();
    Optional<Photo> delete(Long id);
    Optional<Photo> updateThumbnail(Long id, MultipartFile file);
    Optional<Photo> updateProfile(Long id, MultipartFile file);
}
