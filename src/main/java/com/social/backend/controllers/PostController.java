package com.social.backend.controllers;

import com.social.backend.entities.User;
import com.social.backend.payloads.post.PostCreateDto;
import com.social.backend.payloads.post.PostResponseDto;
import com.social.backend.payloads.post.PostUpdateDto;
import com.social.backend.repositories.UserRepo;
import com.social.backend.security.userdetails.CustomUserDetails;
import com.social.backend.services.PostService;
import com.social.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PostResponseDto> createPost(
            @Valid @RequestBody PostCreateDto postDto
    ) {
        PostResponseDto response = postService.createPost(postDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{postId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateDto postDto
    ) {
        PostResponseDto response = postService.updatePost(postId, postDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        User user = null;

        if (userDetails != null) {
            user = userRepo.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        return ResponseEntity.ok(postService.getPosts(user, page, size));
    }

    // user posts endpoint
    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                postService.getPostsByUser(username, page, size)
        );
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }

    @PostMapping("/{userId}/like/{postId}")
    @PreAuthorize("hasRole('ADMIN') or #userDetails != null and #userDetails.id == #userId")
    public ResponseEntity<String> likePost(
            @PathVariable Long userId,
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userService.likePost(userId, postId);
        return ResponseEntity.ok("Post liked");
    }

    @PostMapping("/{userId}/dislike/{postId}")
    @PreAuthorize("hasRole('ADMIN') or #userDetails != null and #userDetails.id == #userId")
    public ResponseEntity<String> dislikePost(
            @PathVariable Long userId,
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userService.dislikePost(userId, postId);
        return ResponseEntity.ok("Post disliked");
    }
}
