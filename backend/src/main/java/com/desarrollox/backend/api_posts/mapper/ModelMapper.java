package com.desarrollox.backend.api_posts.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.desarrollox.backend.api_models.controller.dto.ModelDto;
import com.desarrollox.backend.api_posts.model.PostModel;

@Component
public class ModelMapper {

    public List<ModelDto> toModelDto(List<PostModel> byPostId) {
        return byPostId.stream()
                .map(postModel -> ModelDto.builder()
                    .id(postModel.getModel().getId())
                    .name(postModel.getModel().getName())
                    .biography(postModel.getModel().getBiography())
                    .build()
                ).collect(Collectors.toList());
    }
    
}