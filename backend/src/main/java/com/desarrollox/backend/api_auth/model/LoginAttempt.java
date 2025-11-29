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
@Table(name = "login_attempts")
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "successful", nullable = false)
    private boolean successful;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "attempt_time", nullable = false)
    private LocalDateTime attemptTime;

    @Column(name = "user_agent")
    private String userAgent;

    @PrePersist
    protected void prePersist(){
        this.attemptTime = LocalDateTime.now();
    }

    public static LoginAttempt successful(String email, String ipAddress, String userAgent) {
        return LoginAttempt.builder()
            .email(email)
            .ipAddress(ipAddress)
            .successful(true)
            .userAgent(userAgent)
            .build();
    }

    public static LoginAttempt failed(String email, String ipAddress, String userAgent, String reason) {
        return LoginAttempt.builder()
            .email(email)
            .ipAddress(ipAddress)
            .successful(false)
            .failureReason(reason)
            .userAgent(userAgent)
            .build();
    }
    
}