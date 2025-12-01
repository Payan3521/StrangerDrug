package com.desarrollox.backend.api_notifications.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.desarrollox.backend.api_notifications.controller.dto.NotificationDto;
import com.desarrollox.backend.api_notifications.exception.NotificationNotFoundException;
import com.desarrollox.backend.api_notifications.model.Notification;
import com.desarrollox.backend.api_notifications.repository.INotificationRepository;
import com.desarrollox.backend.api_purchases.exception.PurchaseNotFoundException;
import com.desarrollox.backend.api_purchases.model.Purchase;
import com.desarrollox.backend.api_purchases.repository.IPurchaseRepository;
import com.desarrollox.backend.api_register.exception.UserNotFoundException;
import com.desarrollox.backend.api_register.model.User;
import com.desarrollox.backend.api_register.repository.IRegisterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
    private final INotificationRepository notificationRepository;
    private final IRegisterRepository registerRepository;
    private final IPurchaseRepository purchaseRepository;

    @Override
    public Notification send(NotificationDto notificationDto) {
        if (!purchaseRepository.existsById(notificationDto.getPurchaseId())) {
            throw new PurchaseNotFoundException(
                    "La compra con id: " + notificationDto.getPurchaseId() + " no fue encontrada");
        }
        if (!registerRepository.existsById(notificationDto.getSendUserId())) {
            throw new UserNotFoundException(
                    "El usuario con id: " + notificationDto.getSendUserId() + " no fue encontrado");
        }
        if (!registerRepository.existsById(notificationDto.getReceiverUserId())) {
            throw new UserNotFoundException(
                    "El usuario con id: " + notificationDto.getReceiverUserId() + " no fue encontrado");
        }
        Purchase purchase = purchaseRepository.findById(notificationDto.getPurchaseId()).get();
        User userSend = registerRepository.findById(notificationDto.getSendUserId()).get();
        User userReceive = registerRepository.findById(notificationDto.getReceiverUserId()).get();
        Notification notificationCreate = Notification.builder()
                .message(notificationDto.getMessage())
                .purchase(purchase)
                .senderUser(userSend)
                .receiverUser(userReceive)
                .build();
        Notification saved = notificationRepository.save(notificationCreate);
        return saved;
    }

    @Override
    public List<Notification> receive(Long id) {
        if(!registerRepository.findById(id).isPresent()){
            throw new UserNotFoundException("User not found");
        }
        return notificationRepository.findByReceiverUser_id(id);
    }

    @Override
    public Optional<Notification> delete(Long id) {
        if (notificationRepository.existsById(id)) {
            Optional<Notification> notification = notificationRepository.findById(id);
            notificationRepository.delete(notification.get());
            return Optional.of(notification.get());
        } else {
            throw new NotificationNotFoundException(
                    "La notificación con id: " + id + " no fue encontrada, por lo que no se pudo eliminar");
        }
    }

    @Override
    @Transactional
    public Void clear(Long id) {
        int count = notificationRepository.deleteAllByReceiverUserId(id);
        if (count == 0) {
            throw new NotificationNotFoundException(
                    "No se encontraron notificaciones para eliminar del usuario con id: " + id);
        }
        return null;
    }
}
