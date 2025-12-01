package com.desarrollox.backend.api_models.service;

import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import com.desarrollox.backend.api_models.controller.dto.ModelDto;
import com.desarrollox.backend.api_models.model.Model;

public interface IModelService {
    Model create(ModelDto modelDto, MultipartFile file);
    Optional<Model> delete(Long id);
    Optional<Model> update(Long id, ModelDto modelDto, MultipartFile file);
    Optional<Model> findById(Long id);
    List<Model> findByName(String name);
    List<Model> findAll();
    List<Model> findSalientsModels();
}