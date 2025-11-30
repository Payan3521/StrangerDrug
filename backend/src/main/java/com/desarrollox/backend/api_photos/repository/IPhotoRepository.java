package com.desarrollox.backend.api_photos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_photos.model.Photo;

@Repository
public interface IPhotoRepository extends JpaRepository<Photo, Long> {
    
}