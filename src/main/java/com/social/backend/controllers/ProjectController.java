package com.social.backend.controllers;

import com.social.backend.payloads.ProjectDto;
import com.social.backend.security.userdetails.CustomUserDetails;
import com.social.backend.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/users/{userId}/projects")
    @PreAuthorize("hasRole('ADMIN') or #userDetails != null and #userDetails.id == #userId")
    public ResponseEntity<ProjectDto> addProject(
            @PathVariable Long userId,
            @Valid @RequestBody ProjectDto projectDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ProjectDto createdProject = projectService.addProject(userId, projectDto);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/projects/{projectId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectDto projectDto
    ) {
        // ownership checks should be enforced in service layer if needed
        ProjectDto updatedProject = projectService.updateProject(projectId, projectDto);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/projects/{projectId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        // ownership checks should be enforced in service layer if needed
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
