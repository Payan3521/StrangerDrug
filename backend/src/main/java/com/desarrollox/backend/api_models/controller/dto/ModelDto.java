package com.desarrollox.backend.api_models.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModelDto {
    private Long id;
    private String name;
    private String biography;
}