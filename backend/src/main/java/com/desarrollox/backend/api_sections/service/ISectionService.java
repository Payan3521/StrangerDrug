package com.desarrollox.backend.api_sections.service;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend.api_sections.model.Section;

public interface ISectionService {
    Section create(Section section);

    List<Section> findAll();

    Optional<Section> findById(Long id);

    Optional<Section> delete(Long id);

    Optional<Section> update(Long id, Section section);
}
