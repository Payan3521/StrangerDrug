package com.desarrollox.backend.api_models.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.desarrollox.backend.api_models.controller.dto.ModelDto;
import com.desarrollox.backend.api_models.exception.ModelNotFoundException;
import com.desarrollox.backend.api_models.model.Model;
import com.desarrollox.backend.api_models.repository.IModelRepository;
import com.desarrollox.backend.api_photos.model.Photo;
import com.desarrollox.backend.api_photos.service.IPhotoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModelService implements IModelService {

    private final IModelRepository modelRepository;
    private final IPhotoService photoService;

    @Override
    public Model create(ModelDto modelDto, MultipartFile file) {
        Photo profile = photoService.uploadProfile(file);

        Model model = new Model();
        model.setName(modelDto.getName());
        model.setBiography(modelDto.getBiography());
        model.setProfile(profile);

        Model saved = modelRepository.save(model);
        return saved;
    }

    @Override
    public Optional<Model> delete(Long id) {
        if (modelRepository.existsById(id)) {
            Optional<Model> model = modelRepository.findById(id);
            modelRepository.delete(model.get());
            photoService.delete(model.get().getProfile().getId());
            return Optional.of(model.get());
        } else {
            throw new ModelNotFoundException(
                    "El/La modelo con id: " + id + " no fue encontrad@, por lo que no se pudo eliminar");
        }
    }

    @Override
    public Optional<Model> update(Long id, ModelDto modelDto, MultipartFile file) {
        if (modelRepository.existsById(id)) {
            Model updated = modelRepository.findById(id).get();
            Optional<Photo> profile = photoService.updateProfile(updated.getProfile().getId(), file);

            updated.setName(modelDto.getName());
            updated.setBiography(modelDto.getBiography());
            updated.setProfile(profile.get());

            Model saved = modelRepository.save(updated);
            return Optional.of(saved);
        } else {
            throw new ModelNotFoundException(
                    "El/La modelo con id: " + id + " No fue encontrad@, por lo que no se pudo modificar");
        }
    }

    @Override
    public Optional<Model> findById(Long id) {
        if (modelRepository.existsById(id)) {
            return Optional.of(modelRepository.findById(id).get());
        } else {
            throw new ModelNotFoundException("El/La modelo con id: " + id + " No fue encontrad@");
        }
    }

    @Override
    public List<Model> findByName(String name) {
        return modelRepository.findByName(name);
    }

    @Override
    public List<Model> findAll() {
        return modelRepository.findAll();
    }

    @Override
    public List<Model> findSalientsModels() {
        return modelRepository.findRandom3Models();
    }
    
}
