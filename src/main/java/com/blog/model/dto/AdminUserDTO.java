package com.blog.model.dto;

import com.blog.model.category.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AdminUserDTO {
    private Long id;
    private String name;
    private String email;
    private UserType userType;
    private String bio;
    private String profilePictureUrl;
    private LocalDateTime createdDateTime;
}
