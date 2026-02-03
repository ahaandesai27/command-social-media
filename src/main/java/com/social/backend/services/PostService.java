package com.social.backend.services;

import com.social.backend.entities.User;
import com.social.backend.payloads.post.PostCreateDto;
import com.social.backend.payloads.post.PostResponseDto;
import com.social.backend.payloads.post.PostUpdateDto;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostCreateDto postDto);
    PostResponseDto updatePost(Long postId, PostUpdateDto postDto);
    List<PostResponseDto> getPosts(User user, Integer page, Integer size);
    List<PostResponseDto> getPostsByUser(String username, Integer page, Integer size);
    void deletePost(Long postId);
}
