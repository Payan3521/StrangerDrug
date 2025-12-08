package com.desarrollox.backend.api_purchases.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.desarrollox.backend.api_purchases.model.Purchase;

@Repository
public interface IPurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByUser_IdAndStatusPurchaseClienteIsTrue(Long buyerId);

    List<Purchase> findByStatusPurchaseAdminIsTrue();

    // Cuenta cuántas compras ACTIVAS (para cliente) están asociadas a un payment
    @Query(value = "SELECT COUNT(*) FROM purchases WHERE payment_id = :paymentId AND status_purchase_cliente = TRUE", nativeQuery = true)
    int countByPaymentId(@Param("paymentId") Long paymentId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE purchases SET status_purchase_cliente = false WHERE id = :id", nativeQuery = true)
    int softDeleteCliente(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE purchases SET status_purchase_admin = false WHERE id = :id", nativeQuery = true)
    int softDeleteAdmin(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE purchases SET status_purchase_admin = false", nativeQuery = true)
    int softDeleteAll();

    // consulta con query native que busca si el usuario ha comprado el video Y está
    // activa para el cliente
    @Query(value = "SELECT COUNT(*) FROM purchases WHERE user_id = :userId AND video_id = :videoId AND status_purchase_cliente = TRUE", nativeQuery = true)
    int hasUserPurchasedVideo(@Param("userId") Long userId, @Param("videoId") Long videoId);

}