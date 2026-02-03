package com.social.backend.repositories;

import com.social.backend.entities.Post;
import com.social.backend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {
    Page<Post> findByUserUsername(String username, Pageable pageable);
    // works because spring JPA can traverse nested properties
}
