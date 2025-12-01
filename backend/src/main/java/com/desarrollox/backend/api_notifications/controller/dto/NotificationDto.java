package com.desarrollox.backend.api_notifications.controller.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private String message;
    private Long purchaseId;
    private Long sendUserId;
    private Long receiverUserId;
}
