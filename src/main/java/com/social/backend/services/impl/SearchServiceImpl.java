package com.social.backend.services.impl;

import com.social.backend.entities.Post;
import com.social.backend.entities.User;
import com.social.backend.payloads.post.PostResponseDto;
import com.social.backend.services.SearchService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.hibernate.Session;
import jakarta.persistence.EntityManager;
import org.hibernate.search.mapper.orm.Search;
import java.util.List;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    private final EntityManager entityManager;      // a persistence context manager

    public SearchServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public List<String> searchUsernames(String query, int page, int size) {
        return Search.session((Session) entityManager)
                .search(User.class)
                .select(f -> f.field("username", String.class))
                .where(f -> f.match()
                        .field("username")
                        .matching(query))
                .fetch(page, size)
                .hits();
    }

    @Transactional
    @Override
    public List<PostResponseDto> searchPosts(String query, int page, int size) {
        int offset = page * size;

        // search tokenises by word
        // so single word search will work
        // but not for usernames, partial word search wont work
        List<Post> posts = Search.session((Session) entityManager)
                .search(Post.class)
                .where(f -> f.bool(b -> {
                    b.should(f.match().field("title").boost(1.5f).matching(query));
                    b.should(f.match().field("description").boost(1.0f).matching(query));
                }))
                .sort(f -> f.field("createdAt").desc())
                .fetchHits(offset, size);

        return posts.stream()
                .map(this::toDto)
                .toList();
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
