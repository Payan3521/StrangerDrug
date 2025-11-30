package com.desarrollox.backend.api_purchases.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.desarrollox.backend.api_purchases.model.Purchase;

public interface IPurchaseRepository extends JpaRepository<Purchase, Long> {

    //consulta con query native que busca si el usuario ha comprado el video
    @Query(value = "SELECT COUNT(*) FROM purchases WHERE user_id = :userId AND video_id = :videoId", nativeQuery = true)
    int hasUserPurchasedVideo(@Param("userId") Long userId, @Param("videoId") Long videoId);
    
}