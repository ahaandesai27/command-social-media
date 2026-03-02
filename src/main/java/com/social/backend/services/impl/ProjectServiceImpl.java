package com.social.backend.services.impl;

import com.social.backend.entities.Project;
import com.social.backend.entities.User;
import com.social.backend.exceptions.ResourceNotFoundException;
import com.social.backend.payloads.ProjectDto;
import com.social.backend.repositories.ProjectRepo;
import com.social.backend.repositories.UserRepo;
import com.social.backend.services.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    private Project dtoToProject(ProjectDto projectDto) {
        return modelMapper.map(projectDto, Project.class);
    }

    private ProjectDto projectToDto(Project project) {
        return modelMapper.map(project, ProjectDto.class);
    }


    @Override
    public ProjectDto addProject(Long userId, ProjectDto projectDto) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Project project = dtoToProject(projectDto);
        project.setUser(user);

        projectRepo.save(project);
        return projectToDto(project);
    }

    @Override
    public ProjectDto updateProject(Long projectId, ProjectDto projectDto) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID must not be null");
        }

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project", "id", projectId)
                );
        // do not update ID
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setGithubUrl(projectDto.getGithubUrl());

        projectRepo.save(project);
        return projectToDto(project);
    }

    @Override
    public void deleteProject(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID must not be null");
        }

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project", "id", projectId)
                );

        projectRepo.delete(project);
    }
}
