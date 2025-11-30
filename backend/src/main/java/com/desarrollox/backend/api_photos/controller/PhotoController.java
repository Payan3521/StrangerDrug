package com.desarrollox.backend.api_photos.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.desarrollox.backend.api_photos.model.Photo;
import com.desarrollox.backend.api_photos.service.IPhotoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final IPhotoService photoService;
    
    @PostMapping("/upload-thumbnail")
    public ResponseEntity<Photo> uploadThumbnail(@RequestParam("thumbnail") MultipartFile file){
        Photo photo = photoService.uploadThumbnail(file);
        return new ResponseEntity<>(photo, HttpStatus.CREATED);
    }

    @PostMapping("/upload-profile")
    public ResponseEntity<Photo> uploadProfile(@RequestParam("profile") MultipartFile file){
        Photo photo = photoService.uploadProfile(file);
        return new ResponseEntity<>(photo, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Photo> findById(@PathVariable Long id){
        Optional<Photo> photo = photoService.findById(id);
        return new ResponseEntity<>(photo.get(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Photo>> findAll(){
        List<Photo> photos = photoService.findAll();
        if(photos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

    @PutMapping("/update-thumbnail/{id}")
    public ResponseEntity<Photo> updateThumbnail(@PathVariable Long id, @RequestParam("thumbnail") MultipartFile file){
        Optional<Photo> photoUpdated = photoService.updateThumbnail(id, file);
        return new ResponseEntity<>(photoUpdated.get(), HttpStatus.OK);
    }

    @PutMapping("/update-profile/{id}")
    public ResponseEntity<Photo> updateProfile(@PathVariable Long id, @RequestParam("profile") MultipartFile file){
        Optional<Photo> photoUpdated = photoService.updateProfile(id, file);
        return new ResponseEntity<>(photoUpdated.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Photo> delete(@PathVariable Long id){
        Optional<Photo> photoDeleted = photoService.delete(id);
        return new ResponseEntity<>(photoDeleted.get(), HttpStatus.OK);
    }
}