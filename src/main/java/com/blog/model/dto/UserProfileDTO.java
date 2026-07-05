package com.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String name;
    private String bio;
    private String profilePictureUrl;
}
