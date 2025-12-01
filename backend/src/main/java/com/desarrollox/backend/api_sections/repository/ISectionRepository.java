package com.desarrollox.backend.api_sections.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_sections.model.Section;

@Repository
public interface ISectionRepository extends JpaRepository<Section, Long> {
    boolean existsByName(String name);
    Section findByName(String name);
}