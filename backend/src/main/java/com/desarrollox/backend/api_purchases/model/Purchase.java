package com.desarrollox.backend.api_purchases.model;

import java.time.LocalDateTime;
import com.desarrollox.backend.api_payment.model.Payment;
import com.desarrollox.backend.api_register.model.User;
import com.desarrollox.backend.api_videos.model.Video;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = false)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "id", nullable = false)
    private Video video;

    @Column(name = "status_purchase_admin", nullable = false)
    private boolean statusPurchaseAdmin;

    @Column(name = "status_purchase_cliente", nullable = false)
    private boolean statusPurchaseCliente;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    
}