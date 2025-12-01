package com.desarrollox.backend.api_posts.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_posts.model.PostModel;

@Repository
public interface IPostModelRepository extends JpaRepository<PostModel, Long> {
    List<PostModel> findByPostId(Long postId);
    List<PostModel> findByModel_NameContainingIgnoreCase(String name);
}
