package com.social.backend.services;

import com.social.backend.payloads.FollowDto;

import java.util.List;

public interface UserFollowService {
    void followUser(FollowDto followDto);
    void unfollowUser(FollowDto followDto);
    List<String> getFollowing(String username);
    List<String> getFollowers(String username);
    // will just show usernames, nothing else required
}
