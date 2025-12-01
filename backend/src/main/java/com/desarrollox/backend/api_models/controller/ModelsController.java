package com.desarrollox.backend.api_models.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.desarrollox.backend.api_models.controller.dto.ModelDto;
import com.desarrollox.backend.api_models.model.Model;
import com.desarrollox.backend.api_models.service.IModelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/models")
public class ModelsController {

    private final IModelService modelService;
    
    @PostMapping
    public ResponseEntity<Model> create(@Valid @ModelAttribute ModelDto modelDto, @RequestPart("profile") MultipartFile file) {
        Model saved = modelService.create(modelDto, file);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Model> delete(@PathVariable Long id) {
        modelService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Model> update(@PathVariable Long id,@Valid @ModelAttribute ModelDto modelDto, @RequestParam("profile") MultipartFile file) {
        Optional<Model> updated = modelService.update(id, modelDto, file);
        return new ResponseEntity<>(updated.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Model> findById(@PathVariable Long id) {
        Optional<Model> model = modelService.findById(id);
        return new ResponseEntity<>(model.get(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Model>> findAll() {
        List<Model> modelList = modelService.findAll();
        if (modelList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(modelList, HttpStatus.OK);
    }

    @GetMapping("/name")
    public ResponseEntity<List<Model>> findByName(@PathVariable String name){
        List<Model> modelList = modelService.findByName(name);
        if (modelList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(modelList, HttpStatus.OK);
    }

    @GetMapping("/salients-models")
    public ResponseEntity<List<Model>> findSalientsModels(){
        List<Model> modelList = modelService.findSalientsModels();
        if (modelList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(modelList, HttpStatus.OK);
    }
}