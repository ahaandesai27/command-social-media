package com.social.backend.payloads.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
// for creation of posts
public class PostCreateDto {
    @NotBlank
    @Size(max=200)
    private String title;

    @NotBlank
    @Size(min=200)
    private String description;

    @NotBlank
    private String username;
}
