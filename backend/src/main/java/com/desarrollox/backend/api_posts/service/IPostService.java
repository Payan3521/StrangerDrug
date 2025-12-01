package com.desarrollox.backend.api_posts.service;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend.api_posts.controller.dto.PostDto;
import com.desarrollox.backend.api_posts.controller.dto.PostResponseDto;

public interface IPostService {
    PostResponseDto savePost(PostDto postDto);
    Optional<PostResponseDto> deletePost(Long id);
    Optional<PostResponseDto> updatePost(Long id, PostDto postDto);
    Optional<PostResponseDto> getPost(Long id);
    List<PostResponseDto> getAllPosts();
    List<PostResponseDto> getPostByModelName(String modelName);
    List<PostResponseDto> getPostBySectionName(String sectionName);
    List<PostResponseDto> getPostByTitle(String title);
    List<PostResponseDto> getPostByRecent();
}
