package com.social.backend.payloads.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// ID or username can be provided in req parameters
// Password can be handled separately
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDto {
    private String name;
    private String title;
    private String about;
    private String location;
    private String avatar;
    private List<String> techStack;
}
