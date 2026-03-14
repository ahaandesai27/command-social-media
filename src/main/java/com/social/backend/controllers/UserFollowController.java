package com.social.backend.controllers;

import com.social.backend.payloads.FollowDto;
import com.social.backend.services.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follow")
public class UserFollowController {

    @Autowired
    private UserFollowService userFollowService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> followUser(@RequestBody FollowDto followDto) {
        userFollowService.followUser(followDto);
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> unfollowUser(@RequestBody FollowDto followDto) {
        userFollowService.unfollowUser(followDto);
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @GetMapping("/following/{username}")
    public ResponseEntity<List<String>> getFollowing(@PathVariable String username) {
        return ResponseEntity.ok(userFollowService.getFollowing(username));
    }

    @GetMapping("/followers/{username}")
    public ResponseEntity<List<String>> getFollowers(@PathVariable String username) {
        return ResponseEntity.ok(userFollowService.getFollowers(username));
    }
}
