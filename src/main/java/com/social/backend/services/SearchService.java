package com.social.backend.services;

import com.social.backend.payloads.post.PostResponseDto;
import com.social.backend.payloads.user.UserResponseDto;

import java.util.List;

public interface SearchService {
    List<String> searchUsernames(String query, int page, int size);
    List<PostResponseDto> searchPosts(String query, int page, int size);
}
