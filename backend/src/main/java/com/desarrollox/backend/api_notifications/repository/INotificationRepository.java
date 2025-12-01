package com.desarrollox.backend.api_notifications.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_notifications.model.Notification;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverUser_id(Long receiveUserId);

    @Modifying
    @Query(value = "DELETE FROM notifications WHERE receiver_user_id = :receiverUserId", nativeQuery = true)
    int deleteAllByReceiverUserId(@Param("receiverUserId") Long receiverUserId);
}
