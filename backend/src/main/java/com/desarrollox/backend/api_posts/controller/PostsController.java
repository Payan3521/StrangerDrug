package com.desarrollox.backend.api_posts.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend.api_posts.controller.dto.PostDto;
import com.desarrollox.backend.api_posts.controller.dto.PostResponseDto;
import com.desarrollox.backend.api_posts.service.IPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {

    private final IPostService postService;
    
    @PostMapping
    public ResponseEntity<PostResponseDto> savePost(
        @Valid @ModelAttribute PostDto postDto
    ) { 
        PostResponseDto postResponseDTO = postService.savePost(postDto);
        return new ResponseEntity<>(postResponseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
        @PathVariable Long id, 
        @Valid @ModelAttribute PostDto postDto) {
        Optional<PostResponseDto> post = postService.updatePost(id, postDto);
        return new ResponseEntity<>(post.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> findById(@PathVariable Long id){
        Optional<PostResponseDto> post = postService.getPost(id);
        return new ResponseEntity<>(post.get(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> findAll(){
        List<PostResponseDto> posts = postService.getAllPosts();
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/model-name")
    public ResponseEntity<List<PostResponseDto>> findByModelName(@RequestParam String modelName){
        List<PostResponseDto> posts = postService.getPostByModelName(modelName);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/section-name")
    public ResponseEntity<List<PostResponseDto>> findBySectionName(@RequestParam String sectionName){
        List<PostResponseDto> posts = postService.getPostBySectionName(sectionName);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<List<PostResponseDto>> findByTitle(@RequestParam String title){
        List<PostResponseDto> posts = postService.getPostByTitle(title);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<PostResponseDto>> findRecent(){
        List<PostResponseDto> posts = postService.getPostByRecent();
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}