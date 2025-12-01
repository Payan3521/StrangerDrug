package com.desarrollox.backend.api_posts.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_posts.model.PostModel;

@Repository
public interface IPostModelRepository extends JpaRepository<PostModel, Long> {
    List<PostModel> findByPostId(Long postId);

    @Query("SELECT pm FROM PostModel pm WHERE LOWER(pm.model.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<PostModel> findByModelName(@Param("name") String name);
}
