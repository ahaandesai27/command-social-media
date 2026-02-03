package com.social.backend.controllers;

import com.social.backend.payloads.post.PostResponseDto;
import com.social.backend.services.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/users")
    public List<String> searchUsers(
            @RequestParam String query,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return searchService.searchUsernames(query, page, size);
    }

    @GetMapping("/posts")
    public List<PostResponseDto> searchPosts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return searchService.searchPosts(query, page, size);
    }
}
