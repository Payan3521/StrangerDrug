package com.desarrollox.backend.api_videos.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.desarrollox.backend.api_purchases.repository.IPurchaseRepository;
import com.desarrollox.backend.api_register.exception.UserNotFoundException;
import com.desarrollox.backend.api_register.model.User;
import com.desarrollox.backend.api_register.repository.IRegisterRepository;
import com.desarrollox.backend.api_videos.exception.VideoNotFoundException;
import com.desarrollox.backend.api_videos.model.Video;
import com.desarrollox.backend.api_videos.repository.IVideoRepository;
import com.desarrollox.backend.core.aws.IAwsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class VideoService implements IVideoService {

    private final IVideoRepository videoRepository;
    private final IAwsService awsService;
    private final IRegisterRepository registerRepository;
    private final IPurchaseRepository purchaseRepository;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    
    @Override
    public Video uploadVideo(MultipartFile file) {
        String key = awsService.uploadToS3(file, "videos/mains");

        Video video = Video.builder()
            .s3Bucket(bucketName)
            .s3Key(key)
            .type(Video.VideoType.MAIN)
            .build();
        
        return videoRepository.save(video);
    }

    @Override
    public Video uploadPreview(MultipartFile file) {
        String key = awsService.uploadToS3(file, "videos/previews");
        String url = String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);

        Video video = Video.builder()
            .s3Bucket(bucketName)
            .s3Key(key)
            .s3Url(url)
            .type(Video.VideoType.PREVIEW)
            .build();
        
        return videoRepository.save(video);
    }

    @Override
    public Optional<Video> findById(Long id) {
        Optional<Video> video = videoRepository.findById(id);
        if(video.isEmpty()){
            throw new VideoNotFoundException("Video not found");
        }
        return video;
    }

    @Override
    public List<Video> findAll() {
        return videoRepository.findAll();
    }

    @Override
    public Optional<Video> delete(Long id) {
        Optional<Video> video = videoRepository.findById(id);
        if(video.isEmpty()){
            throw new VideoNotFoundException("Video not found");
        }
        awsService.deleteFromS3(video.get().getS3Key());
        videoRepository.delete(video.get());
        return video;
    }

    @Override
    public Optional<Video> updateVideo(Long id, MultipartFile file) {
        Optional<Video> video = videoRepository.findById(id);
        if(video.isEmpty()){
            throw new VideoNotFoundException("Video not found");
        }
        if(video.get().getType() != Video.VideoType.MAIN){
            throw new VideoNotFoundException("Video type is not main");
        }
        awsService.deleteFromS3(video.get().getS3Key());
        String key = awsService.uploadToS3(file, "videos/mains");
        video.get().setS3Key(key);
        videoRepository.save(video.get());
        return video;
    }

    @Override
    public Optional<Video> updatePreview(Long id, MultipartFile file) {
        Optional<Video> video = videoRepository.findById(id);
        if(video.isEmpty()){
            throw new VideoNotFoundException("Video not found");
        }
        if(video.get().getType() != Video.VideoType.PREVIEW){
            throw new VideoNotFoundException("Video type is not preview");
        }
        awsService.deleteFromS3(video.get().getS3Key());
        String key = awsService.uploadToS3(file, "videos/previews");
        String url = String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
        video.get().setS3Key(key);
        video.get().setS3Url(url);
        videoRepository.save(video.get());
        return video;
    }

    @Override
    public Optional<Video> getPlayableUrl(String videoKey, String email) {
        Video video = videoRepository.findByS3Key(videoKey).orElseThrow(() -> new VideoNotFoundException("Video not found"));
        String role = registerRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found")).getRole().toString();
        if(video.getType().equals(Video.VideoType.PREVIEW)){
            return Optional.of(video);
        }

        if(role.equals("ADMIN")){
            return Optional.of(Video.builder()
                .s3Bucket(video.getS3Bucket())
                .s3Key(video.getS3Key())
                .s3Url(awsService.getPresignedUrl(video.getS3Key()))
                .type(video.getType())
                .build()
            );
        }

        if(verifyPurchase(email, video.getId())){
            return Optional.of(Video.builder()
                .s3Bucket(video.getS3Bucket())
                .s3Key(video.getS3Key())
                .s3Url(awsService.getPresignedUrl(video.getS3Key()))
                .type(video.getType())
                .build()
            );
        }
        throw new VideoNotFoundException("No tienes permiso para ver este video");
    }

    private boolean verifyPurchase(String email, Long videoId){
        User user = registerRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        
        if(purchaseRepository.hasUserPurchasedVideo(user.getId(), videoId) > 0){
            return true;
        }
        return false;
    }
    
}