package com.social.backend.controllers;

import com.social.backend.payloads.comment.CommentCreateDto;
import com.social.backend.payloads.comment.CommentResponseDto;
import com.social.backend.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CommentResponseDto> addComment(
            @RequestBody CommentCreateDto commentDto
    ) {
        CommentResponseDto response = commentService.addComment(commentDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<CommentResponseDto> comments =
                commentService.getCommentsByPostId(postId, page, size);

        return ResponseEntity.ok(comments);
    }
}
