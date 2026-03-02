package com.social.backend.services.impl;

import com.social.backend.entities.Post;
import com.social.backend.entities.User;
import com.social.backend.exceptions.ResourceNotFoundException;
import com.social.backend.payloads.comment.CommentResponseDto;
import com.social.backend.payloads.post.PostCreateDto;
import com.social.backend.payloads.post.PostResponseDto;
import com.social.backend.payloads.post.PostUpdateDto;
import com.social.backend.repositories.PostRepo;

import com.social.backend.repositories.UserRepo;
import com.social.backend.services.PostService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    private Post createDtoToPost(PostCreateDto postCreateDto) {
        return this.modelMapper.map(postCreateDto, Post.class);
    }

    private PostResponseDto postToResponseDto(Post post) {
        PostResponseDto dto = new PostResponseDto();

        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setLikes(post.getLikes());
        dto.setDislikes(post.getDislikes());
        dto.setUsername(post.getUser().getUsername());
        dto.setCreatedAt(post.getCreatedAt());

        return dto;
    }


    private Post updateDtoToPost(PostUpdateDto postUpdateDto) {
        return this.modelMapper.map(postUpdateDto, Post.class);
    }

    private PostResponseDto updatePostToResponseDto(Post post) {
        return this.modelMapper.map(post, PostResponseDto.class);
    }

    @Override
    public PostResponseDto createPost(PostCreateDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());

        User user = this.userRepo.findByUsername(postDto.getUsername())
                        .orElseThrow(() -> new ResourceNotFoundException("User", "username", postDto.getUsername()));
        post.setUser(user);

        Post savedPost = postRepo.save(post);
        return postToResponseDto(savedPost);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateDto postDto) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID cannot be null!");
        }
        Post existingPost = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        if (postDto.getTitle() != null) {
            existingPost.setTitle(postDto.getTitle());
        }
        if (postDto.getDescription() != null) {
            existingPost.setDescription(postDto.getDescription());
        }
        return updatePostToResponseDto(existingPost);
    }

    @Override
    public List<PostResponseDto> getPosts(User user, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Post> posts = postRepo.findAll(pageable).getContent();

        Set<Long> likedPostIds = user == null
                ? Set.of()
                : user.getLikedPosts()
                .stream()
                .map(Post::getId)
                .collect(Collectors.toSet());

        return posts.stream()
                .map(post -> {
                    PostResponseDto dto = postToResponseDto(post);
                    dto.setLiked(likedPostIds.contains(post.getId()));
                    return dto;
                })
                .toList();
    }

    @Override
    public List<PostResponseDto> getPostsByUser(String username, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Post> posts = postRepo.findByUserUsername(username, pageable).getContent();
        return posts.stream().map(this::postToResponseDto).toList();
    }

    @Override
    public void deletePost(Long postId) {
        this.postRepo.deleteById(postId);
    }
}
