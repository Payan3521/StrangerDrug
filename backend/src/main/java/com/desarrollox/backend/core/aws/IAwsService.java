package com.desarrollox.backend.core.aws;

import org.springframework.web.multipart.MultipartFile;

public interface IAwsService {
    String uploadToS3(MultipartFile file, String folder);
    void deleteFromS3(String key);
    String getPresignedUrl(String key);
}