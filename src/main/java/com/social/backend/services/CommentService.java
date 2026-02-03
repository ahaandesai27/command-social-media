package com.social.backend.services;

import com.social.backend.payloads.comment.CommentCreateDto;
import com.social.backend.payloads.comment.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    CommentResponseDto addComment(CommentCreateDto commentDto);
    List<CommentResponseDto> getCommentsByPostId(Long postId, Integer page, Integer size);
}
