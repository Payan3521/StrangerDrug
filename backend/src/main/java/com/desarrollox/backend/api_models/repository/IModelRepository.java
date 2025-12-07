package com.desarrollox.backend.api_models.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_models.model.Model;

@Repository
public interface IModelRepository extends JpaRepository<Model, Long> {
    @Query("SELECT m FROM Model m WHERE LOWER(m.name) LIKE LOWER(CONCAT(:name, '%'))")
    List<Model> findByName(@Param("name") String name);

    @Query(value = "SELECT * FROM models ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Model> findRandom5Models();
}