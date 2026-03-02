package com.social.backend.services.impl;

import com.social.backend.entities.Comment;
import com.social.backend.entities.Post;
import com.social.backend.entities.User;
import com.social.backend.exceptions.ResourceNotFoundException;
import com.social.backend.payloads.comment.CommentCreateDto;
import com.social.backend.payloads.comment.CommentResponseDto;
import com.social.backend.repositories.CommentRepo;
import com.social.backend.repositories.PostRepo;
import com.social.backend.repositories.UserRepo;
import com.social.backend.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Override
    public CommentResponseDto addComment(CommentCreateDto commentDto) {
        User user = userRepo.findByUsername(commentDto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", commentDto.getUsername()));

        Post post = postRepo.findById(commentDto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", commentDto.getPostId()));

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUser(user);
        comment.setPost(post);
        Comment savedComment = commentRepo.save(comment);

        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setContent(savedComment.getContent());
        responseDto.setUsername(user.getUsername());
        responseDto.setPostId(post.getId());
        responseDto.setLikes(savedComment.getLikes());
        return responseDto;
    }

    @Override
    public List<CommentResponseDto> getCommentsByPostId(Long postId, Integer page, Integer size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Comment> commentsPage = commentRepo.findByPostId(postId, pageable);
        List<Comment> comments = commentsPage.getContent();

        return comments.stream().map(comment -> {
            CommentResponseDto dto = new CommentResponseDto();
            dto.setContent(comment.getContent());
            dto.setUsername(comment.getUser().getUsername());
            dto.setPostId(comment.getPost().getId());
            dto.setLikes(comment.getLikes());
            return dto;
        }).toList();
    }
}
