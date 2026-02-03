package com.social.backend.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FollowDto {
    // follower follows followee
    @NotBlank
    private String followee;

    @NotBlank
    private String follower;
}
