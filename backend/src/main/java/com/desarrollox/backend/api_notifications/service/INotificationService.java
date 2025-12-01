package com.desarrollox.backend.api_notifications.service;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend.api_notifications.controller.dto.NotificationDto;
import com.desarrollox.backend.api_notifications.model.Notification;

public interface INotificationService {
    Notification send(NotificationDto notificationDto);
    List<Notification> receive(Long id);
    Optional<Notification> delete(Long id);
    Void clear(Long id);
}
