package com.social.backend.repositories;

import com.social.backend.entities.Post;
import com.social.backend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {
    Page<Post> findByUserUsername(String username, Pageable pageable);
    // works because spring JPA can traverse nested properties

    @Query(
            value = """
            SELECT *
            FROM post
            WHERE posts_search @@ websearch_to_tsquery('english', :keyword)
            ORDER BY ts_rank(posts_search, websearch_to_tsquery('english', :keyword)) DESC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM post
            WHERE posts_search @@ websearch_to_tsquery('english', :keyword)
            """,
            nativeQuery = true
    )
    Page<Post> search(@Param("keyword") String keyword, Pageable pageable);
}
