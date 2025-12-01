package com.desarrollox.backend.api_posts.controller.dto;

import lombok.Data;

@Data
public class ModelResponseDto {
    private Long id;
    private String name;
    private String photoUrl;
    private String biography;
}
