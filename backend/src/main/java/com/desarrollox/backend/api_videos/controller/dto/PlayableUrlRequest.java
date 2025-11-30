package com.desarrollox.backend.api_videos.controller.dto;

import lombok.Data;

@Data
public class PlayableUrlRequest {
    private Long videoId;
    private String email;
}