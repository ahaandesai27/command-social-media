package com.social.backend.services.impl;

import com.social.backend.entities.Post;
import com.social.backend.entities.User;
import com.social.backend.payloads.post.PostResponseDto;
import com.social.backend.repositories.PostRepo;
import com.social.backend.repositories.UserRepo;
import com.social.backend.services.SearchService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.hibernate.Session;
import jakarta.persistence.EntityManager;
import org.hibernate.search.mapper.orm.Search;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Transactional
    @Override
    public List<String> searchUsernames(String query, int page, int size) {
        return this.userRepo.search(query, PageRequest.of(page, size))
                .getContent();
    }

    @Override
    public List<PostResponseDto> searchPosts(String query, int page, int size) {
        return this.postRepo.search(query, PageRequest.of(page, size))
                .map(this::toDto)
                .getContent();
    }

    private PostResponseDto toDto(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setLikes(post.getLikes());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUsername(post.getUser().getUsername());
        return dto;
    }

}
