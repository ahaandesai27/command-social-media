package com.social.backend.services.impl;

import com.social.backend.entities.User;
import com.social.backend.payloads.FollowDto;
import com.social.backend.repositories.UserRepo;
import com.social.backend.services.UserFollowService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private UserRepo userRepo;

    @Transactional      // replaces usage of .save()
    @Override
    public void followUser(FollowDto followDto) {
        User followeeUser = this.userRepo.findByUsername(followDto.getFollowee()).orElseThrow();
        User followerUser = this.userRepo.findByUsername(followDto.getFollower()).orElseThrow();

        if (followerUser.equals(followeeUser)) {
            throw new IllegalArgumentException("User cannot follow themselves!");
        }

        boolean added = followerUser.getFollowingUsers().add(followeeUser);
        if (!added) return;

        followerUser.setFollowing(followerUser.getFollowing() + 1);
        followeeUser.setFollowers(followeeUser.getFollowers() + 1);
    }

    @Transactional
    @Override
    public void unfollowUser(FollowDto followDto) {
        User followeeUser = userRepo.findByUsername(followDto.getFollowee()).orElseThrow();
        User followerUser = userRepo.findByUsername(followDto.getFollower()).orElseThrow();

        if (followerUser.equals(followeeUser)) {
            throw new IllegalArgumentException("User cannot unfollow themselves");
        }

        boolean removed = followerUser.getFollowingUsers().remove(followeeUser);

        if (!removed) {
            return; // was not following, nothing to do
        }

        followerUser.setFollowing(followerUser.getFollowing() - 1);
        followeeUser.setFollowers(followeeUser.getFollowers() - 1);
    }

    @Override
    public List<String> getFollowing(String username) {
        // Gets the users that this user is following
        return this.userRepo.findFollowingUsernames(username);
    }

    @Override
    public List<String> getFollowers(String username) {
        return this.userRepo.findFollowerUsernames(username);
    }
}
