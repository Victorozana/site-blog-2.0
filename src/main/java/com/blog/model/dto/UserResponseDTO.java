package com.blog.model.dto;

import com.blog.model.category.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private UserType userType;
}
