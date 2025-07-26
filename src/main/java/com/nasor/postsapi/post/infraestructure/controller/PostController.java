package com.nasor.postsapi.post.infraestructure.controller;

import com.nasor.postsapi.post.infraestructure.dto.PostResponseDto;
import com.nasor.postsapi.post.infraestructure.dto.PostRegisterRequestDto;
import com.nasor.postsapi.post.infraestructure.dto.PostUpdateRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostController {
    ResponseEntity<PostResponseDto> createPost(PostRegisterRequestDto postRegisterRequestDto);
    ResponseEntity<PostResponseDto> updatePost(Long id, PostUpdateRequestDto postUpdateRequestDto);
    ResponseEntity<List<PostResponseDto>> getPostsByUserId(Long userId);
    ResponseEntity<PostResponseDto> getPostById(Long postId);
    ResponseEntity<Void> deletePost(Long id);
    ResponseEntity<List<PostResponseDto>> getAllPosts();
}
