package com.desarrollox.backend.api_auth.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_auth.model.RefreshToken;
import jakarta.transaction.Transactional;

@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String refreshToken);
    
    // Consulta Nativa para revocar tokens por email
    @Modifying
    @Transactional
    @Query(value = "UPDATE refresh_tokens SET revoked = TRUE, revoked_at = CURRENT_TIMESTAMP() WHERE user_email = :email AND revoked = FALSE", 
           nativeQuery = true)
    void revokeAllByUserEmail(@Param("email") String email);
    
    // Consulta Nativa para eliminar tokens expirados
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM refresh_tokens WHERE expiry_date < :now",
           nativeQuery = true)
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
}
