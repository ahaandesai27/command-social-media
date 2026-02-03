package com.social.backend.payloads.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {
    private String content;
    private String username;
    private Long postId;
    private Integer likes;
}
