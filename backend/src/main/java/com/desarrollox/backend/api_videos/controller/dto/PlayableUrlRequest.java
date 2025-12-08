package com.desarrollox.backend.api_videos.controller.dto;

import lombok.Data;

@Data
public class PlayableUrlRequest {
    private String videoKey;
    private String email;
}