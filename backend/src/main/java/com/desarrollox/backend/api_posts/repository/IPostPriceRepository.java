package com.desarrollox.backend.api_posts.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_posts.model.PostPrice;

@Repository
public interface IPostPriceRepository extends JpaRepository<PostPrice, Long> {
    List<PostPrice> findByPostId(Long postId);
}
