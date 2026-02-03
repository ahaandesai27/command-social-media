package com.social.backend.payloads.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

// used for registration
@NoArgsConstructor
@Getter
@Setter
public class UserCreateDto {
    @NotBlank
    @Length(min = 3, max = 50)
    private String username;

    @NotBlank
    @Length(min = 6, max = 100)
    private String password;
}
