package com.desarrollox.backend.api_posts.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponseDto {
    private Long id;
    private String name;
    private String description;
}
