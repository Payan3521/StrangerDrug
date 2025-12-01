package com.desarrollox.backend.api_sections.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend.api_sections.model.Section;
import com.desarrollox.backend.api_sections.service.ISectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
public class SectionsController {

    private final ISectionService sectionService;
    
    @PostMapping
    public ResponseEntity<Section> create(@Valid @RequestBody Section section) {
        Section saved = sectionService.create(section);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Section>> findAll() {
        List<Section> sections = sectionService.findAll();
        if (sections.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sections, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Section> findById(@PathVariable Long id) {
        Optional<Section> section = sectionService.findById(id);
        return new ResponseEntity<>(section.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sectionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Section> update(@PathVariable Long id, @Valid @RequestBody Section section) {
        Optional<Section> updated = sectionService.update(id, section);
        return new ResponseEntity<>(updated.get(), HttpStatus.OK);
    }
}
