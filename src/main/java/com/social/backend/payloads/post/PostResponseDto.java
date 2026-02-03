package com.social.backend.payloads.post;

import com.social.backend.payloads.comment.CommentResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
// no validators required here
public class PostResponseDto {

    private Long id;
    private String title;
    private String description;
    private int likes;
    private int dislikes;
    private String username;
    private Date createdAt;

    private boolean liked = false;     // for current user viewing
}
