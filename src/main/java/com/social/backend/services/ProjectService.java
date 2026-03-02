package com.social.backend.services;

import com.social.backend.payloads.ProjectDto;

public interface ProjectService {
    ProjectDto addProject(Long userId, ProjectDto projectDto);
    ProjectDto updateProject(Long projectId, ProjectDto projectDto);
    void deleteProject(Long projectId);
}
