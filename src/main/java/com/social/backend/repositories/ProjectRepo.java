package com.social.backend.repositories;

import com.social.backend.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    List<Project> findByUserId(Long userId);

}
