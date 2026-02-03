package com.social.backend.services;

import com.social.backend.payloads.user.UserCreateDto;
import com.social.backend.payloads.user.UserResponseDto;
import com.social.backend.payloads.user.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserCreateDto user);
    List<UserResponseDto> getUsers();
    UserResponseDto getUser(Integer userId);
    UserResponseDto updateUser(UserUpdateDto user, Integer userId);
    UserResponseDto partialUpdateUser(UserUpdateDto user, Integer userId);
    UserResponseDto getUserByUsername(String username);
    void deleteUser(Integer userId);
    void likePost(Integer userId, Long postId);
    void dislikePost(Integer userId, Long postId);
}
