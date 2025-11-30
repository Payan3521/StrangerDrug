package com.desarrollox.backend.core.aws;

import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class AwsService implements IAwsService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    
    @Override
    public String uploadToS3(MultipartFile file, String folder) {
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String key = folder + "/" + filename;

        try {
            PutObjectRequest putObj = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

            s3Client.putObject(
                putObj,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return key;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    @Override
    public void deleteFromS3(String key) {
        s3Client.deleteObject(b -> b.bucket(bucketName).key(key));
    }
    
}
