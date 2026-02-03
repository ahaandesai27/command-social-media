package com.social.backend.services;

import com.social.backend.payloads.ProjectDto;

public interface ProjectService {
    ProjectDto addProject(Integer userId, ProjectDto projectDto);
    ProjectDto updateProject(Integer projectId, ProjectDto projectDto);
    void deleteProject(Integer projectId);
}
