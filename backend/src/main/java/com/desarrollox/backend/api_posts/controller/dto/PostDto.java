package com.desarrollox.backend.api_posts.controller.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class PostDto {
    private String title;
    private String description;
    private String sectionName;
    private int duration;
    private String models;
    private String prices;
    private MultipartFile video;
    private MultipartFile preview;
    private MultipartFile thumbnail;
}
