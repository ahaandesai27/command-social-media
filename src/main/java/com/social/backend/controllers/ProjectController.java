package com.social.backend.controllers;

import com.social.backend.payloads.ProjectDto;
import com.social.backend.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/users/{userId}/projects")
    public ResponseEntity<ProjectDto> addProject(
            @PathVariable Long userId,
            @Valid @RequestBody ProjectDto projectDto
    ) {
        ProjectDto createdProject = projectService.addProject(userId, projectDto);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectDto projectDto
    ) {
        ProjectDto updatedProject = projectService.updateProject(projectId, projectDto);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
