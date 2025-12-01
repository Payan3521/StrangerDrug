package com.desarrollox.backend.api_posts.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_posts.model.Post;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT p FROM Post p WHERE LOWER(p.section.name) LIKE LOWER(CONCAT(:name, '%'))")
    List<Post> findBySectionName(@Param("name") String name);

    @Query(value = "SELECT * FROM posts ORDER BY created_at DESC LIMIT 5", nativeQuery = true)
    List<Post> find5PostRecents();
}
