package com.social.backend.controllers;

import com.social.backend.payloads.user.UserCreateDto;
import com.social.backend.payloads.user.UserResponseDto;
import com.social.backend.payloads.user.UserUpdateDto;
import com.social.backend.security.userdetails.CustomUserDetails;
import com.social.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto userDto) {
        UserResponseDto createdUserDto = this.userService.createUser(userDto);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> users = this.userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        UserResponseDto user = this.userService.getUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = this.userService.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userDetails != null and #userDetails.id == #id")
    // this means either admin can update or self user can update
    public ResponseEntity<UserResponseDto> updateUserById(
            @PathVariable Long id,
            @RequestBody UserUpdateDto userDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserResponseDto updatedUser = this.userService.updateUser(userDto, id);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userDetails != null and #userDetails.id == #id")
    public ResponseEntity<UserResponseDto> partialUpdateUserById(
            @PathVariable Long id,
            @RequestBody UserUpdateDto userDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserResponseDto updatedUser = this.userService.partialUpdateUser(userDto, id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return ResponseEntity.ok("User Deleted Successfully");
    }

}
