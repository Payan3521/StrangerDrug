package com.desarrollox.backend.api_auth.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true, length = 500)
    private String token;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;


    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean isValid(){
        return !revoked && !isExpired();
    }

    public void revoke(){
        this.revoked = true;
        this.revokedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.revoked = false;
    }
    
}