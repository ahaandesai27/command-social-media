package com.social.backend.repositories;

import com.social.backend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("""
         SELECT u.username
         FROM User u 
         JOIN u.followingUsers f        
         WHERE f.username = :username
            """)
    // table / association is u -> f
    // we want to return u such that f = input username
    // u.followingUsers is the set of users that u follows
    // returns who follows the users
    List<String> findFollowerUsernames(@Param("username") String username);

    @Query("""
         SELECT f.username
         FROM User u 
         JOIN u.followingUsers f        
         WHERE u.username = :username
            """)
    // for u -> f
    // returns who the user follows
    List<String> findFollowingUsernames(@Param("username") String username);

    @Query(
            value = """
            SELECT username
            FROM users
            WHERE LOWER(username) LIKE LOWER(:keyword) || '%'
            ORDER BY username;
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM users
            WHERE username_search @@ websearch_to_tsquery('english', :keyword)
            """,
            nativeQuery = true
    )
    Page<String> search(@Param("keyword") String keyword, Pageable pageable);
}
