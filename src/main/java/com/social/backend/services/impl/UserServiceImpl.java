package com.social.backend.services.impl;

import com.social.backend.entities.Post;
import com.social.backend.entities.User;
import com.social.backend.exceptions.ConflictException;
import com.social.backend.exceptions.ResourceNotFoundException;
import com.social.backend.payloads.ProjectDto;
import com.social.backend.payloads.user.UserCreateDto;
import com.social.backend.payloads.user.UserResponseDto;
import com.social.backend.payloads.user.UserUpdateDto;
import com.social.backend.repositories.PostRepo;
import com.social.backend.repositories.UserRepo;
import com.social.backend.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;



import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserResponseDto userToDto(User user) {
        UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
        dto.setFollowers(user.getFollowers());
        dto.setFollowing(user.getFollowing());

        if (user.getProjects() != null) {
            dto.setProjects(
                    user.getProjects()
                            .stream()
                            .map(p -> modelMapper.map(p, ProjectDto.class))
                            .toList()
            );
        }

        return dto;
    }


    @Override
    public UserResponseDto createUser(UserCreateDto userDto) {
        if (userRepo.existsByUsername(userDto.getUsername())) {
            throw new ConflictException("User with given username already exists!");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User savedUser = userRepo.save(user);
        return userToDto(savedUser);
    }

    @Override
    public List<UserResponseDto> getUsers() {
        List<User> users = this.userRepo.findAll();
        return users.stream().map(this::userToDto).toList();
    }

    @Override
    public UserResponseDto getUser(Long userId) {
        User user = this.userRepo.findById(userId).orElseThrow();
        return this.userToDto(user);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = this.userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return this.userToDto(user);
    }

    public UserResponseDto updateUser(UserUpdateDto userDto, Long userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Map all fields from DTO to entity (overwrites everything)
        user.setName(userDto.getName());
        user.setTitle(userDto.getTitle());
        user.setAbout(userDto.getAbout());
        user.setLocation(userDto.getLocation());
        user.setAvatar(userDto.getAvatar());
        user.setTechStack(userDto.getTechStack());

        User updatedUser = this.userRepo.save(user);
        return this.userToDto(updatedUser);
    }

    public UserResponseDto partialUpdateUser(UserUpdateDto userDto, Long userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getTitle() != null) {
            user.setTitle(userDto.getTitle());
        }
        if (userDto.getAbout() != null) {
            user.setAbout(userDto.getAbout());
        }
        if (userDto.getLocation() != null) {
            user.setLocation(userDto.getLocation());
        }
        if (userDto.getAvatar() != null) {
            user.setAvatar(userDto.getAvatar());
        }
        if (userDto.getTechStack() != null) {
            user.setTechStack(userDto.getTechStack());
        }

        User updatedUser = this.userRepo.save(user);
        return this.userToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        this.userRepo.deleteById(userId);
        // no-op if user does not exist
    }


    // Like and Dislike Post methods
    @Override
    public void likePost(Long userId, Long postId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        post.setLikes(post.getLikes() + 1);
        user.getLikedPosts().add(post);
        this.postRepo.save(post);
    }

    @Override
    public void dislikePost(Long userId, Long postId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        post.setLikes(Math.max(0, post.getLikes() - 1));
        user.getLikedPosts().remove(post);
        this.postRepo.save(post);
    }


}
