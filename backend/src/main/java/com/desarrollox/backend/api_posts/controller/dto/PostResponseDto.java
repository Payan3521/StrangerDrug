package com.desarrollox.backend.api_posts.controller.dto;

import java.util.List;
import com.desarrollox.backend.api_models.controller.dto.ModelDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String description;
    private String videoKey;
    private String previewUrl;
    private String thumbnailUrl;
    private SectionResponseDto section;
    private int duration;
    private List<ModelDto> models;
    private List<PriceDto> prices;
}
