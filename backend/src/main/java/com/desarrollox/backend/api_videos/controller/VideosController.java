package com.desarrollox.backend.api_videos.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.desarrollox.backend.api_videos.controller.dto.PlayableUrlRequest;
import com.desarrollox.backend.api_videos.model.Video;
import com.desarrollox.backend.api_videos.service.IVideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideosController {

    private final IVideoService videoService;

    @PostMapping("/upload-video")
    public ResponseEntity<Video> uploadVideo(@RequestParam("video") MultipartFile file){
        Video video = videoService.uploadVideo(file);
        return new ResponseEntity<>(video, HttpStatus.CREATED);
    }

    @PostMapping("/upload-preview")
    public ResponseEntity<?> uploadPreview(@RequestParam("preview") MultipartFile file){
        Video video = videoService.uploadPreview(file);
        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> findById(@PathVariable Long id){
        Optional<Video> video = videoService.findById(id);
        return new ResponseEntity<>(video.get(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Video>> findAll(){
        List<Video> videos = videoService.findAll();
        if(videos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @GetMapping("/get-playable-url")
    public ResponseEntity<Video> getPlayableUrl(@Valid @RequestBody PlayableUrlRequest request){
        Optional<Video> video = videoService.getPlayableUrl(request.getVideoId(), request.getEmail());
        return new ResponseEntity<>(video.get(), HttpStatus.OK);
    }

    @PutMapping("/update-video/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable Long id, @RequestParam("video") MultipartFile file){
        Optional<Video> videoUpdated = videoService.updateVideo(id, file);
        return new ResponseEntity<>(videoUpdated.get(), HttpStatus.OK);
    }

    @PutMapping("/update-preview/{id}")
    public ResponseEntity<Video> updatePreview(@PathVariable Long id, @RequestParam("preview") MultipartFile file){
        Optional<Video> videoUpdated = videoService.updatePreview(id, file);
        return new ResponseEntity<>(videoUpdated.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Video> delete(@PathVariable Long id){
        Optional<Video> videoDeleted = videoService.delete(id);
        return new ResponseEntity<>(videoDeleted.get(), HttpStatus.OK);
    }
}