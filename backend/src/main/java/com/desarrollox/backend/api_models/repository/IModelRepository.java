package com.desarrollox.backend.api_models.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_models.model.Model;

@Repository
public interface IModelRepository extends JpaRepository<Model, Long> {
    List<Model> findByName(String name);

    @Query(value = "SELECT * FROM models ORDER BY RANDOM() LIMIT 3", nativeQuery = true)
    List<Model> findRandom3Models();
}