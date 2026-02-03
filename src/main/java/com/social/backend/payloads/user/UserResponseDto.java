package com.social.backend.payloads.user;

import com.social.backend.payloads.ProjectDto;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {

    private Integer id;
    private String username;

    private String name;
    private String title;
    private String about;
    private String location;

    private Date joinDate;

    private Integer followers;
    private Integer following;

    private String avatar;
    private List<String> techStack;

    @Size(max=3)
    private List<ProjectDto> projects;
}