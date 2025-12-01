package com.desarrollox.backend.api_posts.mapper;

import org.springframework.stereotype.Component;

import com.desarrollox.backend.api_posts.controller.dto.SectionResponseDto;
import com.desarrollox.backend.api_sections.model.Section;

@Component
public class SectionMapper {
    public SectionResponseDto toSectionResponseDto(Section section){
        return SectionResponseDto.builder()
            .id(section.getId())
            .name(section.getName())
            .description(section.getDescription())
            .build();
    }
}
