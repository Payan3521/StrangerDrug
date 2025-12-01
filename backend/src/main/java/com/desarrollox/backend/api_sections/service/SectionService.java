package com.desarrollox.backend.api_sections.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.desarrollox.backend.api_sections.exception.SectionAlreadyRegisteredException;
import com.desarrollox.backend.api_sections.exception.SectionNotFoundException;
import com.desarrollox.backend.api_sections.model.Section;
import com.desarrollox.backend.api_sections.repository.ISectionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionService implements ISectionService{

    private final ISectionRepository sectionRepository;

    @Override
    public Section create(Section section) {
        if (sectionRepository.existsByName(section.getName())) {
            throw new SectionAlreadyRegisteredException("La sección con el nombre '" + section.getName() + "' ya existe.");
        }
        return sectionRepository.save(section);
    }

    @Override
    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    @Override
    public Optional<Section> findById(Long id) {
        if (!sectionRepository.existsById(id)) {
            throw new SectionNotFoundException("La sección con el id '" + id + "' no existe.");
        }
        return sectionRepository.findById(id);
    }

    @Override
    public Optional<Section> delete(Long id) {
        Optional<Section> section = sectionRepository.findById(id);
        if (section.isPresent()) {
            sectionRepository.delete(section.get());
            return section;
        } else {
            throw new SectionNotFoundException("La sección con el id '" + id + "' no existe.");
        }
    }

    @Override
    public Optional<Section> update(Long id, Section section) {
        if (!sectionRepository.existsById(id)) {
            throw new SectionNotFoundException("La sección con el id '" + id + "' no existe.");
        }
        Section existingSection = sectionRepository.findById(id).get();
        existingSection.setName(section.getName());
        existingSection.setDescription(section.getDescription());
        return Optional.of(sectionRepository.save(existingSection));
    }
    
}
