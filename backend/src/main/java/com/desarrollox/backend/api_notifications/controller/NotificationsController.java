package com.desarrollox.backend.api_notifications.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend.api_notifications.controller.dto.NotificationDto;
import com.desarrollox.backend.api_notifications.model.Notification;
import com.desarrollox.backend.api_notifications.service.INotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationsController {

    private final INotificationService notificationService;
    
    @PostMapping
    public ResponseEntity<Notification> send(@Valid @RequestBody NotificationDto notificationDto) {
        Notification saved = notificationService.send(notificationDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{receiveId}")
    public ResponseEntity<List<Notification>> receive(@PathVariable Long receiveId) {
        List<Notification> listNotifications = notificationService.receive(receiveId);
        if (listNotifications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listNotifications, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Notification> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/clear/{receiveId}")
    public ResponseEntity<Void> clear(@PathVariable Long receiveId) {
        notificationService.clear(receiveId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
