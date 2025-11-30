package com.desarrollox.backend.api_photos.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.desarrollox.backend.api_photos.exception.PhotoNotFoundException;
import com.desarrollox.backend.api_photos.model.Photo;
import com.desarrollox.backend.api_photos.repository.IPhotoRepository;
import com.desarrollox.backend.core.aws.IAwsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhotoService implements IPhotoService {

    private final IPhotoRepository photoRepository;
    private final IAwsService awsService;
    
    @Value("${aws.s3.bucketName}")
    private String bucketName;
    
    @Override
    public Photo uploadThumbnail(MultipartFile file) {
        String key = awsService.uploadToS3(file, "images/thumbnails");
        String url = String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);

        Photo photo = Photo.builder()
            .s3Bucket(bucketName)
            .s3Key(key)
            .s3Url(url)
            .type(Photo.PhotoType.THUMBNAIL)
            .build();
        
        return photoRepository.save(photo);
    }

    @Override
    public Photo uploadProfile(MultipartFile file) {
        String key = awsService.uploadToS3(file, "images/profiles");
        String url = String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);

        Photo photo = Photo.builder()
            .s3Bucket(bucketName)
            .s3Key(key)
            .s3Url(url)
            .type(Photo.PhotoType.PROFILE)
            .build();
        
        return photoRepository.save(photo);
    }

    @Override
    public Optional<Photo> findById(Long id) {
        Optional<Photo> photo = photoRepository.findById(id);
        if(photo.isEmpty()){
            throw new PhotoNotFoundException("Photo not found");
        }
        return photo;
    }

    @Override
    public List<Photo> findAll() {
        return photoRepository.findAll();
    }

    @Override
    public Optional<Photo> delete(Long id) {
        Optional<Photo> photo = photoRepository.findById(id);
        if(photo.isEmpty()){
            throw new PhotoNotFoundException("Photo not found");
        }
        awsService.deleteFromS3(photo.get().getS3Key());
        photoRepository.delete(photo.get());
        return photo;
    }

    @Override
    public Optional<Photo> updateThumbnail(Long id, MultipartFile file) {
        Optional<Photo> photo = photoRepository.findById(id);
        if(photo.isEmpty()){
            throw new PhotoNotFoundException("Photo not found");
        }
        if(photo.get().getType() != Photo.PhotoType.THUMBNAIL){
            throw new PhotoNotFoundException("Photo type is not thumbnail");
        }
        awsService.deleteFromS3(photo.get().getS3Key());
        String key = awsService.uploadToS3(file, "images/thumbnails");
        String url = String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
        photo.get().setS3Key(key);
        photo.get().setS3Url(url);
        photoRepository.save(photo.get());
        return photo;
    }
    
    @Override
    public Optional<Photo> updateProfile(Long id, MultipartFile file) {
        Optional<Photo> photo = photoRepository.findById(id);
        if(photo.isEmpty()){
            throw new PhotoNotFoundException("Photo not found");
        }
        if(photo.get().getType() != Photo.PhotoType.PROFILE){
            throw new PhotoNotFoundException("Photo type is not profile");
        }
        awsService.deleteFromS3(photo.get().getS3Key());
        String key = awsService.uploadToS3(file, "images/profiles");
        String url = String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
        photo.get().setS3Key(key);
        photo.get().setS3Url(url);
        photoRepository.save(photo.get());
        return photo;
    }
}