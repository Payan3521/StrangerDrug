package com.desarrollox.backend.api_posts.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.desarrollox.backend.api_models.exception.ModelNotFoundException;
import com.desarrollox.backend.api_models.model.Model;
import com.desarrollox.backend.api_models.repository.IModelRepository;
import com.desarrollox.backend.api_photos.model.Photo;
import com.desarrollox.backend.api_photos.service.IPhotoService;
import com.desarrollox.backend.api_posts.controller.dto.PostDto;
import com.desarrollox.backend.api_posts.controller.dto.PostResponseDto;
import com.desarrollox.backend.api_posts.controller.dto.PriceDto;
import com.desarrollox.backend.api_posts.exception.PostNotFoundException;
import com.desarrollox.backend.api_posts.mapper.ModelMapper;
import com.desarrollox.backend.api_posts.mapper.PriceMapper;
import com.desarrollox.backend.api_posts.mapper.SectionMapper;
import com.desarrollox.backend.api_posts.model.Post;
import com.desarrollox.backend.api_posts.model.PostModel;
import com.desarrollox.backend.api_posts.model.PostPrice;
import com.desarrollox.backend.api_posts.repository.IPostModelRepository;
import com.desarrollox.backend.api_posts.repository.IPostPriceRepository;
import com.desarrollox.backend.api_posts.repository.IPostRepository;
import com.desarrollox.backend.api_sections.exception.SectionNotFoundException;
import com.desarrollox.backend.api_sections.model.Section;
import com.desarrollox.backend.api_sections.repository.ISectionRepository;
import com.desarrollox.backend.api_videos.model.Video;
import com.desarrollox.backend.api_videos.service.IVideoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {

    private final IPostRepository postRepository;
    private final IModelRepository modelRepository;
    private final ISectionRepository sectionRepository;
    private final IVideoService videoService;
    private final IPhotoService photoService;
    private final IPostModelRepository postModelRepository;
    private final IPostPriceRepository postPriceRepository;
    private final SectionMapper sectionMapper;
    private final ModelMapper modelMapper;
    private final PriceMapper priceMapper;

    @Override
    public PostResponseDto savePost(PostDto postDto) {

        ObjectMapper objectMapper = new ObjectMapper();

        List<Long> modelsParseados = null;
        List<PriceDto> pricesParseados = null;
        try {
            if (postDto.getModels() != null && !postDto.getModels().isEmpty()) {
                modelsParseados = objectMapper.readValue(postDto.getModels(), new TypeReference<List<Long>>() {
                });
            }
            if (postDto.getPrices() != null && !postDto.getPrices().isEmpty()) {
                pricesParseados = objectMapper.readValue(postDto.getPrices(), new TypeReference<List<PriceDto>>() {
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar los datos JSON de modelos o precios: " + e.getMessage());
        }

        List<Model> models = List.of();
        if (modelsParseados != null && !modelsParseados.isEmpty()) {
            models = modelRepository.findAllById(modelsParseados);
            if (models.size() != modelsParseados.size()) {
                List<Long> encontrados = models.stream()
                        .map(Model::getId)
                        .toList();

                List<Long> faltantes = modelsParseados.stream()
                        .filter(id -> !encontrados.contains(id))
                        .toList();

                throw new ModelNotFoundException("Las siguientes modelos no se encontraron: " + faltantes);
            }
        }

        if (!sectionRepository.existsByName(postDto.getSectionName())) {
            throw new SectionNotFoundException("La seccion con nombre " + postDto.getSectionName() + " no se encontro");
        }

        Section section = sectionRepository.findByName(postDto.getSectionName());
        Video video = videoService.uploadVideo(postDto.getVideo());
        Video preview = videoService.uploadPreview(postDto.getPreview());
        Photo thumbnail = photoService.uploadThumbnail(postDto.getThumbnail());

        Post post = Post.builder()
                .video(video)
                .videoPreview(preview)
                .thumbnail(thumbnail)
                .title(postDto.getTitle())
                .description(postDto.getDescription())
                .section(section)
                .duration(postDto.getDuration())
                .build();

        postRepository.save(post);

        models.forEach(model -> {
            PostModel postModel = PostModel.builder()
                    .post(post)
                    .model(model)
                    .build();
            postModelRepository.save(postModel);
        });

        if (pricesParseados != null) {
            pricesParseados.forEach(price -> {
                PostPrice postPrice = PostPrice.builder()
                        .post(post)
                        .codeCountry(price.getCodeCountry())
                        .country(price.getCountry())
                        .amount(price.getAmount())
                        .currency(price.getCurrency())
                        .build();
                postPriceRepository.save(postPrice);
            });
        }

        return mapToResponseDto(post);
    }

    @Override
    public Optional<PostResponseDto> deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException("El post con id " + id + " no se encontro");
        }

        Post post = postRepository.findById(id).get();

        // 1. Eliminar relaciones de modelos y precios primero
        List<PostModel> postModels = postModelRepository.findByPostId(id);
        postModelRepository.deleteAll(postModels);

        List<PostPrice> postPrices = postPriceRepository.findByPostId(id);
        postPriceRepository.deleteAll(postPrices);

        // 2. Guardar IDs de recursos para eliminar de S3 después
        Long videoId = post.getVideo().getId();
        Long previewId = post.getVideoPreview().getId();
        Long thumbnailId = post.getThumbnail().getId();

        // 3. Guardar DTO antes de eliminar
        PostResponseDto responseDto = mapToResponseDto(post);

        // 4. Eliminar el post (esto debería permitir eliminar los videos/fotos si no
        // hay otras FK)
        postRepository.deleteById(id);

        // 5. Eliminar recursos de DB y AWS
        // Nota: Si Video/Photo tienen CascadeType.REMOVE desde Post, se eliminarían
        // solos de DB,
        // pero como son entidades independientes, debemos eliminarlos explícitamente.
        // Y como VideoService.delete busca por ID, debemos hacerlo después de borrar el
        // Post
        // para evitar restricción de FK si el Post apunta al Video.
        // PERO: Post tiene FK a Video. Si borramos Video primero, falla.
        // Si borramos Post primero, la FK desaparece. Entonces podemos borrar Video.

        try {
            videoService.delete(videoId);
        } catch (Exception e) {
            // Log error but continue
            System.err.println("Error deleting video: " + e.getMessage());
        }
        try {
            videoService.delete(previewId);
        } catch (Exception e) {
            System.err.println("Error deleting preview: " + e.getMessage());
        }
        try {
            photoService.delete(thumbnailId);
        } catch (Exception e) {
            System.err.println("Error deleting thumbnail: " + e.getMessage());
        }

        return Optional.of(responseDto);
    }

    @Override
    public Optional<PostResponseDto> updatePost(Long id, PostDto postDto) {

        ObjectMapper objectMapper = new ObjectMapper();

        List<Long> modelsParseados = null;
        List<PriceDto> pricesParseados = null;
        try {
            if (postDto.getModels() != null && !postDto.getModels().isEmpty()) {
                modelsParseados = objectMapper.readValue(postDto.getModels(), new TypeReference<List<Long>>() {
                });
            }
            if (postDto.getPrices() != null && !postDto.getPrices().isEmpty()) {
                pricesParseados = objectMapper.readValue(postDto.getPrices(), new TypeReference<List<PriceDto>>() {
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar los datos JSON de modelos o precios: " + e.getMessage());
        }

        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException("El post con id " + id + " no se encontro");
        }

        Post post = postRepository.findById(id).get();

        // Validar modelos
        List<Model> models = List.of();
        if (modelsParseados != null && !modelsParseados.isEmpty()) {
            models = modelRepository.findAllById(modelsParseados);
            if (models.size() != modelsParseados.size()) {
                List<Long> encontrados = models.stream()
                        .map(Model::getId)
                        .toList();

                List<Long> faltantes = modelsParseados.stream()
                        .filter(id2 -> !encontrados.contains(id2))
                        .toList();

                throw new ModelNotFoundException("Las siguientes modelos no se encontraron: " + faltantes);
            }
        }

        // Validar sección
        if (!sectionRepository.existsByName(postDto.getSectionName())) {
            throw new SectionNotFoundException("La seccion con nombre " + postDto.getSectionName() + " no se encontro");
        }

        Section section = sectionRepository.findByName(postDto.getSectionName());

        // Actualizar videos y thumbnail si se proporcionan nuevos archivos
        if (postDto.getVideo() != null && !postDto.getVideo().isEmpty()) {
            videoService.updateVideo(post.getVideo().getId(), postDto.getVideo());
        }

        if (postDto.getPreview() != null && !postDto.getPreview().isEmpty()) {
            videoService.updatePreview(post.getVideoPreview().getId(), postDto.getPreview());
        }

        if (postDto.getThumbnail() != null && !postDto.getThumbnail().isEmpty()) {
            photoService.updateThumbnail(post.getThumbnail().getId(), postDto.getThumbnail());
        }

        // Actualizar datos del post
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setSection(section);
        post.setDuration(postDto.getDuration());

        postRepository.save(post);

        // Actualizar modelos (eliminar los anteriores y agregar los nuevos)
        List<PostModel> oldPostModels = postModelRepository.findByPostId(id);
        postModelRepository.deleteAll(oldPostModels);

        models.forEach(model -> {
            PostModel postModel = PostModel.builder()
                    .post(post)
                    .model(model)
                    .build();
            postModelRepository.save(postModel);
        });

        // Actualizar precios (eliminar los anteriores y agregar los nuevos)
        List<PostPrice> oldPostPrices = postPriceRepository.findByPostId(id);
        postPriceRepository.deleteAll(oldPostPrices);

        if (pricesParseados != null) {
            pricesParseados.forEach(price -> {
                PostPrice postPrice = PostPrice.builder()
                        .post(post)
                        .codeCountry(price.getCodeCountry())
                        .country(price.getCountry())
                        .amount(price.getAmount())
                        .currency(price.getCurrency())
                        .build();
                postPriceRepository.save(postPrice);
            });
        }

        return Optional.of(mapToResponseDto(post));
    }

    @Override
    public Optional<PostResponseDto> getPost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException("El post con id " + id + " no se encontro");
        }
        return postRepository.findById(id)
                .map(this::mapToResponseDto);
    }

    @Override
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public List<PostResponseDto> getPostByModelName(String modelName) {
        List<PostModel> postModels = postModelRepository.findByModel_NameContainingIgnoreCase(modelName);

        return postModels.stream()
                .map(PostModel::getPost)
                .distinct()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public List<PostResponseDto> getPostBySectionName(String sectionName) {
        // Eliminamos la validación estricta para permitir búsqueda parcial
        // if (!sectionRepository.existsByName(sectionName)) { ... }

        return postRepository.findBySectionNameStartingWithIgnoreCase(sectionName).stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public List<PostResponseDto> getPostByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public List<PostResponseDto> getPostByRecent() {

        return postRepository.find5PostRecents().stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    private PostResponseDto mapToResponseDto(Post post) {
        PostResponseDto postResponseDto = PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .duration(post.getDuration())
                .videoKey(post.getVideo().getS3Key())
                .previewUrl(post.getVideoPreview().getS3Url())
                .thumbnailUrl(post.getThumbnail().getS3Url())

                .section(sectionMapper.toSectionResponseDto(post.getSection()))
                .models(modelMapper.toModelDto(postModelRepository.findByPostId(post.getId())))
                .prices(priceMapper.toPriceDto(postPriceRepository.findByPostId(post.getId())))
                .build();

        return postResponseDto;
    }

}