package com.social.backend.repositories;

import com.social.backend.entities.Comment;
import com.social.backend.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {

    Page<Comment> findByPost(Post post, Pageable pageable);

    Page<Comment> findByPostId(Long postId, Pageable pageable);
}
