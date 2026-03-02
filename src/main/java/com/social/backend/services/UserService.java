package com.social.backend.services;

import com.social.backend.payloads.user.UserCreateDto;
import com.social.backend.payloads.user.UserResponseDto;
import com.social.backend.payloads.user.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserCreateDto user);
    List<UserResponseDto> getUsers();
    UserResponseDto getUser(Long userId);
    UserResponseDto updateUser(UserUpdateDto user, Long userId);
    UserResponseDto partialUpdateUser(UserUpdateDto user, Long userId);
    UserResponseDto getUserByUsername(String username);
    void deleteUser(Long userId);
    void likePost(Long userId, Long postId);
    void dislikePost(Long userId, Long postId);
}
