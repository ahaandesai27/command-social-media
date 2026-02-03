package com.social.backend.payloads.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class  CommentCreateDto {
    @NotBlank
    private String username;

    @NotBlank
    private Long postId;

    @NotBlank
    private String content;
}
